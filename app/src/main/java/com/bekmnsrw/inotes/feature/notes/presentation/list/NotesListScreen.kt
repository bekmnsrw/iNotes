package com.bekmnsrw.inotes.feature.notes.presentation.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bekmnsrw.inotes.R
import com.bekmnsrw.inotes.core.navigation.NestedScreen
import com.bekmnsrw.inotes.feature.notes.domain.dto.CardColor
import com.bekmnsrw.inotes.feature.notes.domain.dto.NoteDto
import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import com.bekmnsrw.inotes.feature.notes.presentation.list.NotesListViewModel.*
import com.bekmnsrw.inotes.feature.notes.presentation.list.NotesListViewModel.NotesListScreenAction.*
import com.bekmnsrw.inotes.feature.notes.presentation.list.NotesListViewModel.NotesListScreenEvent.*
import com.bekmnsrw.inotes.feature.notes.util.formatLastModifiedInNotesList
import com.bekmnsrw.inotes.ui.custom.CustomTheme
import com.bekmnsrw.inotes.ui.custom.Theme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
fun NotesListContentPreview() {
    Theme {
        NotesListContent(
            screenState = NotesListScreenState(
                notes = persistentListOf(
                    NoteDto(
                        1,
                        "Coffee",
                        "Prepare hot coffee for friends",
                        false,
                        123,
                        CardColor.BASE,
                        1
                    ),
                    NoteDto(
                        2,
                        "Birthday Party Preparations",
                        "",
                        false,
                        123,
                        CardColor.BASE,
                        1
                    )
                ),
                tags = persistentListOf(TagDto(1, "#all"))
            ),
            eventHandler = {}
        )
    }
}

@Composable
fun NotesListScreen(
    navController: NavController,
    viewModel: NotesListViewModel = hiltViewModel()
) {
    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    val screenAction by viewModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

    NotesListContent(
        screenState = screenState.value,
        eventHandler = viewModel::eventHandler,
    )

    NotesListActions(
        screenAction = screenAction,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesListContent(
    screenState: NotesListScreenState,
    eventHandler: (NotesListScreenEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    eventHandler(OnButtonAddClicked(0))
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        }
    ) { contentPadding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(count = 2),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(contentPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                item(span = StaggeredGridItemSpan.FullLine) {
                    HeadingContent(notesCount = screenState.notes.size)
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    TagsList(
                        selectedTagId = screenState.selectedTagId,
                        tagDtoList = screenState.tags,
                        eventHandler = eventHandler
                    )
                }
                items(
                    items = screenState.notes,
                    key = { it.id }
                ) {
                    NoteListItem(
                        noteDto = it
                    ) { noteId ->
                        eventHandler(OnNoteClicked(noteId))
                    }
                }
            }
        )
    }
}

@Composable
fun NotesListActions(
    screenAction: NotesListScreenAction?,
    navController: NavController
) {
    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is NavigateNoteDetailsScreen -> navController.navigate(
                NestedScreen.NoteDetails.createRoute(screenAction.noteId)
            )

            is NavigateTagsScreen -> navController.navigate(
                NestedScreen.Tags.createRoute(screenAction.tagId)
            )
        }
    }
}

@Composable
fun HeadingContent(
    notesCount: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.title_first_line),
            color = CustomTheme.colors.onBackground,
            style = CustomTheme.typography.screenHeading
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.title_second_line),
                color = CustomTheme.colors.onBackground,
                style = CustomTheme.typography.screenHeading
            )
            Text(
                text = "/$notesCount",
                color = CustomTheme.colors.outline,
                style = CustomTheme.typography.notesCount
            )
        }
    }
}

@Composable
fun TagsList(
    selectedTagId: Long,
    tagDtoList: PersistentList<TagDto>,
    eventHandler: (NotesListScreenEvent) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        FolderCard {
            eventHandler(OnButtonFolderClicked)
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        ) {
            items(
                items = tagDtoList,
                key = { it.id }
            ) {
                TagsListItem(
                    tagDto = it,
                    isSelected = it.id == selectedTagId
                ) { tagId ->
                    eventHandler(OnTagClicked(tagId))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderCard(
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CustomTheme.colors.background),
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = Icons.Outlined.FolderOpen,
            contentDescription = null,
            modifier = Modifier.padding(12.dp),
            tint = CustomTheme.colors.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsListItem(
    tagDto: TagDto,
    isSelected: Boolean,
    onClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier.wrapContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (isSelected) {
                true -> CustomTheme.colors.primary
                false -> CustomTheme.colors.background
            }
        ),
        onClick = { onClick(tagDto.id) }
    ) {
        Text(
            text = tagDto.name,
            color = when (isSelected) {
                true -> CustomTheme.colors.onPrimary
                false -> CustomTheme.colors.onBackground
            },
            style = CustomTheme.typography.tag,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListItem(
    noteDto: NoteDto,
    onClick: (Long) -> Unit
) {
    Card(
        onClick = { onClick(noteDto.id) },
        colors = CardDefaults.cardColors(
            containerColor = when (noteDto.cardColor) {
                CardColor.BASE -> CustomTheme.colors.baseCardColor
                CardColor.BLUE -> CustomTheme.colors.blueCardColor
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (!noteDto.title.isNullOrBlank()) {
                Text(
                    text = noteDto.title,
                    color = CustomTheme.colors.onBackground,
                    style = CustomTheme.typography.cardTitle,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (noteDto.content.isNotBlank()) {
                Text(
                    text = noteDto.content,
                    color = CustomTheme.colors.onBackground,
                    style = CustomTheme.typography.cardContent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = CustomTheme.colors.outline
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = when (noteDto.cardColor) {
                            CardColor.BASE -> CustomTheme.colors.baseCardColor
                            CardColor.BLUE -> CustomTheme.colors.blueCardColor
                        }
                    )
                ) {
                    Text(
                        text = formatLastModifiedInNotesList(noteDto.lastModified),
                        color = CustomTheme.colors.outline,
                        style = CustomTheme.typography.cardDate,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (noteDto.isPinned) {
                        Icon(
                            imageVector = Icons.Rounded.PushPin,
                            contentDescription = null,
                            tint = CustomTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}
