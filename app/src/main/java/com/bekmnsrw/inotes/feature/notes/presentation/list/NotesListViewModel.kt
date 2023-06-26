package com.bekmnsrw.inotes.feature.notes.presentation.list

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bekmnsrw.inotes.feature.notes.presentation.list.NotesListViewModel.NotesListScreenAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor() : ViewModel() {

    private val _screenState = MutableStateFlow(NotesListScreenState())
    val screenState: StateFlow<NotesListScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<NotesListScreenAction?>()
    val screenAction: SharedFlow<NotesListScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(event: NotesListScreenEvent) {
        when (event) {
            is NotesListScreenEvent.OnButtonAddClicked -> navigateNoteDetails(event.noteId)
            is NotesListScreenEvent.OnNoteClicked -> navigateNoteDetails(event.noteId)
            is NotesListScreenEvent.OnTagClicked -> onTagClicked(event.tagId)
        }
    }

    @Immutable
    data class NotesListScreenState(
        val isLoading: Boolean = false,
        val error: Throwable? = null,
        val selectedTagId: Long = 1,
        // ToDo: add notes list
    )

    @Immutable
    sealed interface NotesListScreenEvent {
        data class OnNoteClicked(val noteId: Long) : NotesListScreenEvent
        data class OnButtonAddClicked(val noteId: Long) : NotesListScreenEvent
        data class OnTagClicked(val tagId: Long) : NotesListScreenEvent
    }

    @Immutable
    sealed interface NotesListScreenAction {
        data class NavigateNoteDetails(val noteId: Long) : NotesListScreenAction
    }

    private fun onTagClicked(tagId: Long) = viewModelScope.launch {
        _screenState.emit(_screenState.value.copy(selectedTagId = tagId))
    }

    private fun navigateNoteDetails(noteId: Long) = viewModelScope.launch {
        _screenAction.emit(NavigateNoteDetails(noteId))
    }
}
