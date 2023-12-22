package com.krakos.minijp.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minijp.R
import com.krakos.minijp.ui.screens.HomeScreen
import com.krakos.minijp.ui.screens.MiniJpViewModel
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniJpApp() {
    val miniJpVM: MiniJpViewModel = viewModel(factory = MiniJpViewModel.Factory)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showSearchDialog by remember { mutableStateOf(true) }
    var searchValue by remember { mutableStateOf("") }

    if (showSearchDialog) {
        SearchDialog(
            searchValue = searchValue,
            onSearchValueChange = { searchValue = it },
            onSearchClick = miniJpVM::getWords,
            onDismiss = { showSearchDialog = false },
            onFilterClick = { /* TODO */ },
            filterOptions = listOf()
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { MiniJpTopBar(
            scrollBehavior = scrollBehavior,
            searchValue = searchValue,
            onBackClick = { /* TODO */ },
            onSearchClick = { showSearchDialog = true })
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeScreen(viewModel = miniJpVM, miniJpUiState = miniJpVM.miniJpUiState)
        }
    }
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniJpTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    searchValue: String,
    onBackClick: ()->Unit,
    onSearchClick: ()->Unit
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(text = searchValue)
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_arrow))
            } },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier,
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchDialog(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onDismiss: () -> Unit,
    onFilterClick: () -> Unit,
    filterOptions: List<String>
) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                singleLine = true,
                value = searchValue,
                onValueChange = { onSearchValueChange(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearchClick(searchValue)
                    onDismiss()
                }),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                },
                placeholder = { Text(text = stringResource(id = R.string.search_placeholder)) }
            )

            //Divider()
            FlowRow(
                modifier = Modifier.padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEach {
                    FilterChip(
                        selected = Random.nextBoolean(),
                        onClick = { onFilterClick() },
                        label = { Text(text = it)
                        })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(id = R.string.info_icon),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = stringResource(id = R.string.info_about_search),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MiniJpTopBarPreview() {
    MiniJpTopBar(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        onBackClick = {},
        onSearchClick = {},
        searchValue = "犬"
    )
}


@Preview()
@Composable
fun SearchDialogPreview() {
    SearchDialog(
        searchValue = "",
        onSearchValueChange = {},
        onSearchClick = {},
        onDismiss = {},
        onFilterClick = {},
        filterOptions = listOf("jlpt-n5","jlpt-n4","jlpt-n3","jlpt-n2","jlpt-n1", "common")
    )
}
