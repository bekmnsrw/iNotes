package com.bekmnsrw.inotes.feature.notes.presentation.details

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekmnsrw.inotes.feature.notes.domain.dto.CardColor
import com.bekmnsrw.inotes.feature.notes.domain.dto.NoteDto
import com.bekmnsrw.inotes.feature.notes.domain.usecase.note.GetNoteByIdUseCase
import com.bekmnsrw.inotes.feature.notes.domain.usecase.note.SaveNoteUseCase
import com.bekmnsrw.inotes.feature.notes.domain.usecase.note.UpdateCardColorUseCase
import com.bekmnsrw.inotes.feature.notes.domain.usecase.note.UpdateIsPinnedUseCase
import com.bekmnsrw.inotes.feature.notes.presentation.details.NoteDetailsViewModel.NoteDetailsScreenAction.*
import com.bekmnsrw.inotes.feature.notes.presentation.details.NoteDetailsViewModel.NoteDetailsScreenEvent.*
import com.bekmnsrw.inotes.feature.notes.util.getCurrentDateTimeInMilliseconds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val updateIsPinnedUseCase: UpdateIsPinnedUseCase,
    private val updateCardColorUseCase: UpdateCardColorUseCase
) : ViewModel() {

    companion object {
        private const val NOTE_ID_KEY = "noteId"
        private const val DEFAULT_NOTE_ID = 0L
    }

    init {
        savedStateHandle.get<String>(NOTE_ID_KEY)?.let {
            if (it.toLong() != DEFAULT_NOTE_ID) {
                loadNote(it.toLong())
            }
        }
    }

    private val _screenState = MutableStateFlow(NoteDetailsScreenState())
    val screenState: StateFlow<NoteDetailsScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<NoteDetailsScreenAction?>()
    val screenAction: SharedFlow<NoteDetailsScreenAction?> = _screenAction.asSharedFlow()

    @Immutable
    data class NoteDetailsScreenState(
        val note: NoteDto = NoteDto(
            id = 0L,
            title = "",
            content = "",
            isPinned = false,
            lastModified = 0,
            cardColor = CardColor.BASE,
            tagId = 1L
        ),
        val isTitleEmpty: Boolean = true,
        val isContentEmpty: Boolean = true,
        val isUserTyping: Boolean = false,
        val isColorBottomSheetVisible: Boolean = false
    )

    @Immutable
    sealed interface NoteDetailsScreenEvent {
        data class OnNoteTitleChange(val noteTitle: String) : NoteDetailsScreenEvent
        data class OnNoteContentChange(val noteContent: String) : NoteDetailsScreenEvent
        object OnEmptyNoteTitle : NoteDetailsScreenEvent
        object OnEmptyNoteContent : NoteDetailsScreenEvent
        object OnNotEmptyNoteTitle : NoteDetailsScreenEvent
        object OnNotEmptyNoteContent : NoteDetailsScreenEvent
        object OnUserStartTyping : NoteDetailsScreenEvent
        object OnArrowBackClicked : NoteDetailsScreenEvent
        object OnIconDoneClicked : NoteDetailsScreenEvent
        object OnPushPinClicked : NoteDetailsScreenEvent
        data class OnColorPaletteClicked(val isVisible: Boolean) : NoteDetailsScreenEvent
        data class OnColorCardClicked(val cardColor: CardColor) : NoteDetailsScreenEvent
    }

    @Immutable
    sealed interface NoteDetailsScreenAction {
        object NavigateBack : NoteDetailsScreenAction
    }

    fun eventHandler(event: NoteDetailsScreenEvent) {
        when (event) {
            is OnNoteContentChange -> onNoteContentChange(event.noteContent)
            is OnNoteTitleChange -> onNoteTitleChange(event.noteTitle)
            OnEmptyNoteContent -> onEmptyNoteContent()
            OnEmptyNoteTitle -> onEmptyNoteTitle()
            OnNotEmptyNoteContent -> onNotEmptyNoteContent()
            OnNotEmptyNoteTitle -> onNotEmptyNoteTitle()
            OnUserStartTyping -> onUserStartTyping()
            OnArrowBackClicked -> onArrowBackClicked()
            is OnIconDoneClicked -> onIconDoneClicked()
            OnPushPinClicked -> onPushPinClicked()
            is OnColorPaletteClicked -> onColorPaletteClicked(event.isVisible)
            is OnColorCardClicked -> onColorCardClicked(event.cardColor)
        }
    }

    private fun onColorCardClicked(cardColor: CardColor) = viewModelScope.launch {
        val noteId = _screenState.value.note.id

        updateCardColorUseCase(noteId, cardColor)
            .flowOn(Dispatchers.IO)
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        note = _screenState.value.note.copy(
                            cardColor = cardColor
                        )
                    )
                )
            }
    }

    private fun onColorPaletteClicked(isVisible: Boolean) = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isColorBottomSheetVisible = !isVisible
            )
        )
    }

    private fun onPushPinClicked() = viewModelScope.launch {
        val note = _screenState.value.note

        updateIsPinnedUseCase(note.id, !note.isPinned)
            .flowOn(Dispatchers.IO)
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        note = _screenState.value.note.copy(
                            isPinned = it
                        )
                    )
                )
            }
    }

    private fun loadNote(noteId: Long) = viewModelScope.launch {
        getNoteByIdUseCase(noteId)
            .flowOn(Dispatchers.IO)
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        note = it
                    )
                )
            }
    }

    private fun onArrowBackClicked() = viewModelScope.launch {
        _screenAction.emit(NavigateBack)

        if (!(_screenState.value.isTitleEmpty && _screenState.value.isContentEmpty)) {
            if (_screenState.value.isUserTyping) {
                _screenState.emit(
                    _screenState.value.copy(
                        note = _screenState.value.note.copy(
                            lastModified = getCurrentDateTimeInMilliseconds()
                        )
                    )
                )
                saveNoteUseCase(_screenState.value.note)
                    .flowOn(Dispatchers.IO)
                    .collect()
            }
        }
    }

    private fun onUserStartTyping() = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isUserTyping = true
            )
        )
    }

    private fun onNoteContentChange(noteContent: String) = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                note = _screenState.value.note.copy(
                    content = noteContent
                )
            )
        )
    }

    private fun onEmptyNoteTitle() = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isTitleEmpty = true
            )
        )
    }

    private fun onNotEmptyNoteTitle() = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isTitleEmpty = false
            )
        )
    }

    private fun onEmptyNoteContent() = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isContentEmpty = true
            )
        )
    }

    private fun onNotEmptyNoteContent() = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isContentEmpty = false
            )
        )
    }

    private fun onNoteTitleChange(noteTitle: String) = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                note = _screenState.value.note.copy(
                    title = noteTitle
                )
            )
        )
    }

    private fun onIconDoneClicked() = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isUserTyping = false
            )
        )

        _screenState.emit(
            _screenState.value.copy(
                note = _screenState.value.note.copy(
                    lastModified = getCurrentDateTimeInMilliseconds()
                )
            )
        )

        saveNoteUseCase(_screenState.value.note)
            .flowOn(Dispatchers.IO)
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        note = _screenState.value.note.copy(
                            id = it
                        )
                    )
                )
            }
    }
}
