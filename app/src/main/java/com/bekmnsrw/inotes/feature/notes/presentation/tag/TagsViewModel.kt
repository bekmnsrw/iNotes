package com.bekmnsrw.inotes.feature.notes.presentation.tag

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import com.bekmnsrw.inotes.feature.notes.domain.usecase.tag.CheckIfTagAlreadyExistsByNameUseCase
import com.bekmnsrw.inotes.feature.notes.domain.usecase.tag.GetAllTagsUseCase
import com.bekmnsrw.inotes.feature.notes.domain.usecase.tag.SaveTagUseCase
import com.bekmnsrw.inotes.feature.notes.presentation.list.NotesListViewModel
import com.bekmnsrw.inotes.feature.notes.presentation.tag.TagsViewModel.TagsScreenAction.*
import com.bekmnsrw.inotes.feature.notes.presentation.tag.TagsViewModel.TagsScreenEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val checkIfTagAlreadyExistsByNameUseCase: CheckIfTagAlreadyExistsByNameUseCase,
    private val saveTagUseCase: SaveTagUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 500L
        private const val STOP_TIMEOUT_MILLIS = 5000L

        private const val SELECTED_TAG_ID_KEY = "selectedTagId"
    }

    @Immutable
    data class TagsScreenState(
        val selectedTagId: Long = 1,
        val tags: PersistentList<TagDto> = persistentListOf(),
        val isCreateTagDialogVisible: Boolean = false
    )

    @Immutable
    sealed interface TagsScreenEvent {
        data class OnTagClicked(val id: Long) : TagsScreenEvent
        object OnArrowBackClicked : TagsScreenEvent
        object OnButtonAddClicked : TagsScreenEvent
        data class OnTagPressed(val id: Long) : TagsScreenEvent
        object OnCreateTagDialogDismiss : TagsScreenEvent
        object OnButtonSaveTagClicked : TagsScreenEvent
        data class OnTagNameChanged(val tagName: String) : TagsScreenEvent
    }

    @Immutable
    sealed interface TagsScreenAction {
        data class NavigateNoteListScreen(val selectedTagId: Long) : TagsScreenAction
        object NavigateBack : TagsScreenAction
    }

    private val _screenState = MutableStateFlow(TagsScreenState())
    val screenState: StateFlow<TagsScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<TagsScreenAction?>()
    val screenAction: SharedFlow<TagsScreenAction?> = _screenAction.asSharedFlow()

    private val _tagNameInput = MutableStateFlow("")
    val tagNameInput: StateFlow<String> = _tagNameInput.asStateFlow()

    fun eventHandler(event: TagsScreenEvent) {
        when (event) {
            OnButtonAddClicked -> onButtonAddClicked()
            is OnTagClicked -> onTagClicked(event.id)
            is OnTagPressed -> onTagPressed(event.id)
            OnButtonSaveTagClicked -> onButtonSaveTagClicked()
            OnCreateTagDialogDismiss -> onCreateTagDialogDismiss()
            is OnTagNameChanged -> onTagNameChanged(event.tagName)
            OnArrowBackClicked -> onArrowBackClicked()
        }
    }

    init {
        getAllTags()
        getSelectedTagId()
    }

    private fun onTagPressed(selectedTagId: Long) = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                selectedTagId = selectedTagId
            )
        )
    }

    private fun onTagClicked(tagId: Long) = viewModelScope.launch {
        _screenAction.emit(
            NavigateNoteListScreen(tagId)
        )
    }

    private fun onArrowBackClicked() = viewModelScope.launch {
        _screenAction.emit(NavigateBack)
    }

    private fun getSelectedTagId() = viewModelScope.launch {
        savedStateHandle.get<String>(SELECTED_TAG_ID_KEY)?.let {
            _screenState.emit(
                _screenState.value.copy(
                    selectedTagId = it.toLong()
                )
            )
        }
    }

    private val _isTagAlreadyExists = MutableStateFlow(false)
    val isTagAlreadyExists: StateFlow<Boolean> = tagNameInput
        .debounce(TIMEOUT_MILLIS)
        .distinctUntilChanged()
        .combine(_isTagAlreadyExists) { text, _ ->
            var isExists = false
            checkIfTagAlreadyExistsByNameUseCase(text)
                .flowOn(Dispatchers.IO)
                .collect { isExists = it }
            isExists
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = _isTagAlreadyExists.value
        )

    private fun onButtonSaveTagClicked() = viewModelScope.launch {
        hideDialog()
        resetTagName()

        saveTagUseCase(
            TagDto(
                id = 0L,
                name = _tagNameInput.value,
                noteCount = NotesListViewModel.NOTE_COUNT_INITIAL_VALUE
            )
        )
            .flowOn(Dispatchers.IO)
            .collect()
    }

    private fun onTagNameChanged(tagName: String) = viewModelScope.launch {
        _tagNameInput.value = tagName
    }

    private fun onButtonAddClicked() = viewModelScope.launch {
        showDialog()
    }

    private fun onCreateTagDialogDismiss() = viewModelScope.launch {
        resetTagName()
        hideDialog()
    }

    private fun hideDialog() = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isCreateTagDialogVisible = false
            )
        )
    }

    private fun showDialog() = viewModelScope.launch {
        _screenState.emit(
            _screenState.value.copy(
                isCreateTagDialogVisible = true
            )
        )
    }

    private fun resetTagName() = viewModelScope.launch {
        _tagNameInput.value = ""
    }

    private fun getAllTags() = viewModelScope.launch {
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
}
