package com.bekmnsrw.inotes.feature.notes.presentation.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import com.bekmnsrw.inotes.feature.notes.presentation.tag.TagsViewModel.*
import com.bekmnsrw.inotes.feature.notes.presentation.tag.TagsViewModel.TagsScreenEvent.*
import com.bekmnsrw.inotes.ui.custom.CustomTheme
import com.bekmnsrw.inotes.ui.custom.Theme

@Preview(showBackground = true)
@Composable
fun TagsContentPreview() {
    Theme {

    }
}

@Composable
fun TagsScreen(
    navController: NavController,
    viewModel: TagsViewModel = hiltViewModel()
) {
    val screenState = viewModel.screenState.collectAsStateWithLifecycle()
    val screenAction by viewModel.screenAction.collectAsStateWithLifecycle(initialValue = null)

    TagsContent(
        screenState = screenState.value,
        eventHandler = viewModel::eventHandler
    )

    TagsActions(

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsContent(
    screenState: TagsScreenState,
    eventHandler: (TagsScreenEvent) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    eventHandler(OnButtonAddClicked)
                }
            ) {
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
                .padding(contentPadding)
        ) {
            items(
                items = screenState.tags,
                key = { it.id }
            ) {
                TagsListItem(
                    tagDto = it,
                    isSelected = it.id == screenState.selectedTagId,
                    notesCount = screenState.notesCount[it.id]!!
                ) {
                    eventHandler(OnTagClicked(it.id))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsListItem(
    tagDto: TagDto,
    isSelected: Boolean,
    notesCount: Int,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CustomTheme.colors.background),
        onClick = { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row() {
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
                style = CustomTheme.typography.cardTitle
            )
            Text(
                text = "/$notesCount",
                color = CustomTheme.colors.onBackground,
                style = CustomTheme.typography.cardDate
            )
        }
    }
}

@Composable
fun TagsActions() {

}
