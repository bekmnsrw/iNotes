package com.bekmnsrw.inotes.feature.notes.presentation.details

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor() : ViewModel() {

    private val _screenState = MutableStateFlow(NoteDetailsScreenState())
    val screenState: StateFlow<NoteDetailsScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<NoteDetailsScreenAction?>()
    val screenAction: SharedFlow<NoteDetailsScreenAction?> = _screenAction.asSharedFlow()

    fun eventHandler(event: NoteDetailsScreenEvent) {
        when (event) {
            else -> {}
        }
    }

    @Immutable
    data class NoteDetailsScreenState(
        val isLoading: Boolean = false,
        val error: Throwable? = null,
        // ToDo: add note dto
    )

    @Immutable
    sealed interface NoteDetailsScreenEvent {

    }

    @Immutable
    sealed interface NoteDetailsScreenAction {

    }
}
