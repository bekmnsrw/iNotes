package com.bekmnsrw.inotes.feature.notes.presentation.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bekmnsrw.inotes.R
import com.bekmnsrw.inotes.feature.notes.domain.dto.NoteDto
import com.bekmnsrw.inotes.feature.notes.presentation.details.NoteDetailsViewModel.NoteDetailsScreenAction
import com.bekmnsrw.inotes.feature.notes.presentation.details.NoteDetailsViewModel.NoteDetailsScreenEvent
import com.bekmnsrw.inotes.feature.notes.presentation.details.NoteDetailsViewModel.NoteDetailsScreenEvent.*
import com.bekmnsrw.inotes.feature.notes.presentation.details.NoteDetailsViewModel.NoteDetailsScreenState
import com.bekmnsrw.inotes.ui.custom.CustomTheme
import com.bekmnsrw.inotes.ui.custom.Theme

@Preview(showBackground = true)
@Composable
fun NoteDetailsContentPreview() {
    Theme {
        NoteDetailsContent(
            screenState = NoteDetailsScreenState(),
            eventHandler = {}
        )
    }
}

@Composable
fun NoteDetailsScreen(
    navController: NavController,
    viewModel: NoteDetailsViewModel = hiltViewModel()
) {
    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    val screenAction by viewModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

    NoteDetailsContent(
        screenState = screenState.value,
        eventHandler = viewModel::eventHandler
    )

    NoteDetailsActions(
        screenAction = screenAction,
        navController = navController
    )
}

@Composable
fun NoteDetailsContent(
    screenState: NoteDetailsScreenState,
    eventHandler: (NoteDetailsScreenEvent) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar(
            areTitleAndContentEmpty = screenState.isTitleEmpty && screenState.isContentEmpty,
            isUserTyping = screenState.isUserTyping,
            eventHandler = eventHandler,
            focusManager = focusManager
        )

        NoteHeader(
            note = screenState.note,
            eventHandler = eventHandler,
            focusRequester = focusRequester
        )

        NoteContent(
            note = screenState.note,
            eventHandler = eventHandler,
            focusRequester = focusRequester
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TopBar(
    areTitleAndContentEmpty: Boolean,
    isUserTyping: Boolean,
    eventHandler: (NoteDetailsScreenEvent) -> Unit,
    focusManager: FocusManager
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderIcon(
            imageVector = Icons.Rounded.ArrowBack,
            horizontalPadding = 0
        ) { eventHandler(OnArrowBackClicked) }
        when {
            isUserTyping && !areTitleAndContentEmpty -> {
                HeaderIcon(
                    imageVector = Icons.Rounded.Done,
                    horizontalPadding = 0
                ) {
                    eventHandler(OnIconDoneClicked)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            }

            !areTitleAndContentEmpty -> {
                Row {
                    HeaderIcon(
                        imageVector = Icons.Outlined.PushPin,
                        horizontalPadding = 0
                    ) {}
                    HeaderIcon(
                        imageVector = Icons.Outlined.Palette,
                        horizontalPadding = 8
                    ) {}
                    HeaderIcon(
                        imageVector = Icons.Rounded.MoreVert,
                        horizontalPadding = 0
                    ) {}
                }
            }
        }
    }
}

@Composable
private fun HeaderIcon(
    imageVector: ImageVector,
    horizontalPadding: Int,
    onClick: () -> Unit
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = horizontalPadding.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    )
}

@Composable
fun NoteHeader(
    note: NoteDto,
    eventHandler: (NoteDetailsScreenEvent) -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        note.title?.let { title ->
            BasicTextField(
                value = title,
                onValueChange = { value ->
                    eventHandler(OnNoteTitleChange(noteTitle = value))
                },
                textStyle = CustomTheme.typography.noteTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            eventHandler(OnUserStartTyping)
                        }
                    },
                decorationBox = { innerTextField ->
                    if (note.title.isEmpty()) {
                        eventHandler(OnEmptyNoteTitle)
                        Text(
                            text = stringResource(id = R.string.note_title),
                            color = CustomTheme.colors.outline,
                            style = CustomTheme.typography.noteTitle
                        )
                    } else {
                        eventHandler(OnNotEmptyNoteTitle)
                    }
                    innerTextField()
                }
            )
        }
        Text(
            text = note.lastModified,
            color = CustomTheme.colors.outline,
            style = CustomTheme.typography.cardDate
        )
    }
}

@Composable
fun NoteContent(
    note: NoteDto,
    eventHandler: (NoteDetailsScreenEvent) -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        BasicTextField(
            value = note.content,
            onValueChange = { eventHandler(OnNoteContentChange(noteContent = it)) },
            textStyle = CustomTheme.typography.noteContent,
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester = focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.hasFocus) {
                        eventHandler(OnUserStartTyping)
                    }
                },
            decorationBox = { innerTextField ->
                if (note.content.isEmpty()) {
                    eventHandler(OnEmptyNoteContent)
                    Text(
                        text = stringResource(id = R.string.note_content),
                        color = CustomTheme.colors.outline,
                        style = CustomTheme.typography.noteContent
                    )
                } else {
                    eventHandler(OnNotEmptyNoteContent)
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun NoteDetailsActions(
    screenAction: NoteDetailsScreenAction?,
    navController: NavController
) {
    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit
            NoteDetailsScreenAction.NavigateBack -> navController.navigateUp()
        }
    }
}
