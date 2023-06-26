package com.bekmnsrw.inotes.feature.notes.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bekmnsrw.inotes.feature.notes.presentation.details.NoteDetailsViewModel.*
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
    noteId: Long,
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
        noteId = noteId
    )
}

@Composable
fun NoteDetailsContent(
    screenState: NoteDetailsScreenState,
    eventHandler: (NoteDetailsScreenEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar()
        NoteHeader()
        NoteContent()
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null
        )
        Row {
            Icon(
                imageVector = Icons.Outlined.PushPin,
                contentDescription = null
            )
            Icon(
                imageVector = Icons.Outlined.Palette,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null
            )
        }
    }
}

@Composable
fun NoteHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Заголовок",
            color = CustomTheme.colors.onBackground,
            style = CustomTheme.typography.noteTitle,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "26.06.2023 13:17",
            color = CustomTheme.colors.outline,
            style = CustomTheme.typography.cardDate
        )
    }
}

@Composable
fun NoteContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Контент заметки",
            color = CustomTheme.colors.onBackground,
            style = CustomTheme.typography.noteContent
        )
    }
}

@Composable
fun NoteDetailsActions(
    screenAction: NoteDetailsScreenAction?,
    noteId: Long
) {
    println(noteId)
}
