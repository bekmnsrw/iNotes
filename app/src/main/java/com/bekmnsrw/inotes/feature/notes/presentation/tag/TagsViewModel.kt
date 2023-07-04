package com.bekmnsrw.inotes.feature.notes.presentation.tag

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor() : ViewModel() {

    @Immutable
    data class TagsScreenState(
        val selectedTagId: Long = 1,
        val tags: PersistentList<TagDto> = persistentListOf(),
        val notesCount: PersistentMap<Long, Int> = persistentMapOf()
    )

    @Immutable
    sealed interface TagsScreenEvent {
        data class OnTagClicked(val id: Long) : TagsScreenEvent
        object OnButtonAddClicked : TagsScreenEvent
        data class OnTagPressed(val id: Long) : TagsScreenEvent // ToDo: open bottom sheet with actions (delete, update, pin)
    }

    @Immutable
    sealed interface TagsScreenAction {
        data class NavigateNoteListScreen(val id: Long) : TagsScreenAction
    }

    private val _screenState = MutableStateFlow<TagsScreenState>(TagsScreenState())
    val screenState: StateFlow<TagsScreenState> = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<TagsScreenAction?>()
    val screenAction: SharedFlow<TagsScreenAction?> = _screenAction.asSharedFlow()

    init {

    }

    fun eventHandler(event: TagsScreenEvent) {
        when (event) {
            TagsScreenEvent.OnButtonAddClicked -> TODO()
            is TagsScreenEvent.OnTagClicked -> TODO()
            is TagsScreenEvent.OnTagPressed -> TODO()
        }
    }
}
