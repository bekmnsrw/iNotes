package com.bekmnsrw.inotes.feature.notes.presentation.list

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekmnsrw.inotes.feature.notes.domain.dto.NoteDto
import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import com.bekmnsrw.inotes.feature.notes.domain.usecase.note.GetAllNotesUseCase
import com.bekmnsrw.inotes.feature.notes.domain.usecase.tag.CheckIfTagExistsByIdUseCase
import com.bekmnsrw.inotes.feature.notes.domain.usecase.tag.GetAllTagsUseCase
import com.bekmnsrw.inotes.feature.notes.domain.usecase.tag.SaveTagUseCase
import com.bekmnsrw.inotes.feature.notes.presentation.list.NotesListViewModel.NotesListScreenAction.*
import com.bekmnsrw.inotes.feature.notes.presentation.list.NotesListViewModel.NotesListScreenEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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
class NotesListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val checkIfTagExistsByIdUseCase: CheckIfTagExistsByIdUseCase,
    private val saveTagUseCase: SaveTagUseCase
) : ViewModel() {

    companion object {
        private const val TAG_ALL_NAME = "All"
        const val TAG_ALL_ID = 1L
        const val NOTE_COUNT_INITIAL_VALUE = 0L
    }

    init {
        saveAllTagIfNotExists()
        loadNotes()
        loadTags()
    }

    private val _screenState = MutableStateFlow(NotesListScreenState())
    val screenState: StateFlow<NotesListScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<NotesListScreenAction?>()
    val screenAction: SharedFlow<NotesListScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(event: NotesListScreenEvent) {
        when (event) {
            is OnButtonAddClicked -> navigateNoteDetailsScreenToCreate(event.noteId)
            is OnNoteClicked -> navigateNoteDetailsScreenToUpdate(event.noteId)
            is OnTagClicked -> onTagClicked(event.tagId)
            OnButtonFolderClicked -> onButtonFolderClicked()
        }
    }

    private fun onButtonFolderClicked() = viewModelScope.launch {
        _screenAction.emit(
            NavigateTagsScreen(_screenState.value.selectedTagId)
        )
    }

    private fun saveAllTagIfNotExists() = viewModelScope.launch {
        checkIfTagExistsByIdUseCase(TAG_ALL_ID)
            .flowOn(Dispatchers.IO)
            .collect {
                if (!it) {
                    saveTagUseCase(
                        TagDto(
                            TAG_ALL_ID,
                            TAG_ALL_NAME,
                            NOTE_COUNT_INITIAL_VALUE
                        )
                    )
                        .flowOn(Dispatchers.IO)
                        .collect()
                }
            }
    }

    private fun loadTags() = viewModelScope.launch {
        getAllTagsUseCase()
            .flowOn(Dispatchers.IO)
            .collect {
                _screenState.emit(
                    _screenState.value.copy(
                        tags = it.toPersistentList()
                    )
                )
            }
    }

    private fun loadNotes() = viewModelScope.launch {
        getAllNotesUseCase()
            .flowOn(Dispatchers.IO)
            .collect {
                _screenState.emit(
                    screenState.value.copy(
                        notes = it.toPersistentList()
                    )
                )
            }
    }

    @Immutable
    data class NotesListScreenState(
        val selectedTagId: Long = 1,
        val notes: PersistentList<NoteDto> = persistentListOf(),
        val tags: PersistentList<TagDto> = persistentListOf()
    )

    @Immutable
    sealed interface NotesListScreenEvent {
        data class OnNoteClicked(val noteId: Long) : NotesListScreenEvent
        data class OnButtonAddClicked(val noteId: Long) : NotesListScreenEvent
        data class OnTagClicked(val tagId: Long) : NotesListScreenEvent
        object OnButtonFolderClicked : NotesListScreenEvent
    }

    @Immutable
    sealed interface NotesListScreenAction {
        data class NavigateNoteDetailsScreenToCreate(val noteId: Long, val tagId: Long) : NotesListScreenAction
        data class NavigateNoteDetailsScreenToUpdate(val noteId: Long) : NotesListScreenAction
        data class NavigateTagsScreen(val tagId: Long) : NotesListScreenAction
    }

    private fun onTagClicked(tagId: Long) = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                selectedTagId = tagId
            )
        )
    }

    private fun navigateNoteDetailsScreenToUpdate(noteId: Long) = viewModelScope.launch {
        _screenAction.emit(
            NavigateNoteDetailsScreenToUpdate(noteId)
        )
    }

    private fun navigateNoteDetailsScreenToCreate(noteId: Long) = viewModelScope.launch {
        _screenAction.emit(
            NavigateNoteDetailsScreenToCreate(
                noteId,
                _screenState.value.selectedTagId
            )
        )
    }
}
