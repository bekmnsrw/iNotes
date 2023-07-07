package com.bekmnsrw.inotes.feature.notes.presentation.tag

import android.nfc.Tag
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EditAttributes
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bekmnsrw.inotes.R
import com.bekmnsrw.inotes.core.navigation.BottomNavigationItem
import com.bekmnsrw.inotes.core.navigation.NavigationGraph
import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import com.bekmnsrw.inotes.feature.notes.presentation.tag.TagsViewModel.*
import com.bekmnsrw.inotes.feature.notes.presentation.tag.TagsViewModel.TagsScreenAction.*
import com.bekmnsrw.inotes.feature.notes.presentation.tag.TagsViewModel.TagsScreenEvent.*
import com.bekmnsrw.inotes.ui.custom.CustomTheme
import com.bekmnsrw.inotes.ui.custom.Theme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun TagsContentPreview() {
    Theme {
        TagsContent(
            screenState = TagsScreenState(
                selectedTagId = 1,
                tags = persistentListOf(
                    TagDto(
                        id = 1,
                        name = "All",
                        noteCount = 10
                    ),
                    TagDto(
                        id = 2,
                        name = "Home",
                        noteCount = 0
                    )
                )
            ),
            tagName = "",
            isTagAlreadyExists = false,
            eventHandler = {},
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                rememberTopAppBarState()
            ),
            modalBottomSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden
            ),
            coroutineScope = rememberCoroutineScope()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TagsScreen(
    navController: NavController,
    viewModel: TagsViewModel = hiltViewModel()
) {
    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    val screenAction by viewModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

    val tagName by viewModel.tagNameInput.collectAsStateWithLifecycle()
    val isTagAlreadyExists by viewModel.isTagAlreadyExists.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    val coroutineScope = rememberCoroutineScope()

    TagsContent(
        screenState = screenState.value,
        tagName = tagName,
        isTagAlreadyExists = isTagAlreadyExists,
        eventHandler = viewModel::eventHandler,
        scrollBehavior = scrollBehavior,
        modalBottomSheetState = modalBottomSheetState,
        coroutineScope = coroutineScope
    )

    TagsActions(
        screenAction = screenAction,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TagsContent(
    screenState: TagsScreenState,
    tagName: String,
    isTagAlreadyExists: Boolean,
    eventHandler: (TagsScreenEvent) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modalBottomSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            BottomSheetWithTagActions(
                eventHandler = eventHandler
            )
        },
        sheetShape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp
        )
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TagsScreenTopBar(
                    scrollBehavior = scrollBehavior,
                    eventHandler = eventHandler
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { eventHandler(OnButtonAddClicked) }) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                }
            }
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CustomTheme.colors.background)
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(
                    items = screenState.tags,
                    key = { it.id }
                ) {
                    TagsListItem(
                        tagDto = it,
                        isSelected = it.id == screenState.selectedTagId,
                        notesCount = it.noteCount,
                        onClick = {
                            eventHandler(
                                OnTagClicked(
                                    id = it.id
                                )
                            )
                        },
                        onPress = {
                            eventHandler(
                                OnTagPressed(
                                    id = it.id
                                )
                            )
                            coroutineScope.launch {
                                if (modalBottomSheetState.isVisible) {
                                    modalBottomSheetState.hide()
                                } else {
                                    modalBottomSheetState.show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
    CreateTagDialog(
        tagName = tagName,
        shouldShow = screenState.isCreateTagDialogVisible,
        isTagAlreadyExists = isTagAlreadyExists,
        onConfirmButtonClicked = { eventHandler(OnButtonSaveTagClicked) },
        onDismiss = { eventHandler(OnCreateTagDialogDismiss) },
        eventHandler = eventHandler
    )
}

@Composable
fun BottomSheetWithTagActions(
    eventHandler: (TagsScreenEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(CustomTheme.colors.background)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TagActionItem(
            imageVector = Icons.Outlined.PushPin,
            caption = "Pin"
        ) {

        }
        TagActionItem(
            imageVector = Icons.Outlined.Delete,
            caption = "Delete"
        ) {

        }
        TagActionItem(
            imageVector = Icons.Rounded.Edit,
            caption = "Edit"
        ) {

        }
    }
}

@Composable
private fun TagActionItem(
    imageVector: ImageVector,
    caption: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = CustomTheme.colors.onBackground
        )
        Text(
            text = caption,
            color = CustomTheme.colors.onBackground,
            style = CustomTheme.typography.cardDate
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    eventHandler: (TagsScreenEvent) -> Unit
) = MediumTopAppBar(
    title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.tags_screen_title)
            )
        }
    },
    actions = {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null
            )
        }
    },
    navigationIcon = {
        IconButton(onClick = { eventHandler(OnArrowBackClicked) }) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null
            )
        }
    },
    scrollBehavior = scrollBehavior
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTagDialog(
    tagName: String,
    shouldShow: Boolean,
    isTagAlreadyExists: Boolean,
    onConfirmButtonClicked: () -> Unit,
    onDismiss: () -> Unit,
    eventHandler: (TagsScreenEvent) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    AnimatedVisibility(visible = shouldShow) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(225.dp),
                colors = CardDefaults.cardColors(containerColor = CustomTheme.colors.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.create_tag_dialog_title),
                        color = CustomTheme.colors.onBackground,
                        style = CustomTheme.typography.cardTitle
                    )
                    OutlinedTextField(
                        value = tagName,
                        textStyle = CustomTheme.typography.cardDate,
                        onValueChange = { eventHandler(OnTagNameChanged(tagName = it)) },
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.create_tag_dialog_text_field_hint),
                                style = CustomTheme.typography.cardDate
                            )
                        },
                        supportingText = {
                            AnimatedVisibility(visible = isTagAlreadyExists) {
                                Text(
                                    text = stringResource(id = R.string.create_tag_dialog_tag_already_exists_error),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = CustomTheme.colors.outline)
                        ) {
                            Text(
                                text = stringResource(id = R.string.create_tag_dialog_dismiss_button_text),
                                color = CustomTheme.colors.background,
                                style = CustomTheme.typography.cardDate
                            )
                        }
                        Button(
                            enabled = !isTagAlreadyExists && tagName.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            onClick = onConfirmButtonClicked,
                            colors = ButtonDefaults.buttonColors(containerColor = CustomTheme.colors.primary)
                        ) {
                            Text(
                                text = stringResource(id = R.string.create_tag_dialog_confirm_button_text),
                                color = CustomTheme.colors.background,
                                style = CustomTheme.typography.cardDate
                            )
                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TagsListItem(
    tagDto: TagDto,
    isSelected: Boolean,
    notesCount: Long,
    onClick: () -> Unit,
    onPress: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CustomTheme.colors.background),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onPress
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Outlined.Done,
                        contentDescription = null,
                        tint = CustomTheme.colors.primary
                    )
                }
                Text(
                    text = tagDto.name,
                    color = CustomTheme.colors.onBackground,
                    style = CustomTheme.typography.cardTitle,
                    modifier = Modifier
                        .padding(
                            start = if (isSelected) 0.dp else 40.dp
                        )
                )
            }
            Text(
                text = "/$notesCount",
                color = CustomTheme.colors.onBackground,
                style = CustomTheme.typography.cardDate
            )
        }
    }
}

@Composable
fun TagsActions(
    screenAction: TagsScreenAction?,
    navController: NavController
) {
    LaunchedEffect(screenAction) {
        when (screenAction) {
            null -> Unit

            is NavigateNoteListScreen -> {
                navController.navigate(
                    BottomNavigationItem.NotesList.createRoute(
                        screenAction.selectedTagId
                    )
                ) {
                    popUpTo(NavigationGraph.NotesGraph.route) {
                        inclusive = true
                    }
                }
            }

            NavigateBack -> navController.navigateUp()
        }
    }
}
