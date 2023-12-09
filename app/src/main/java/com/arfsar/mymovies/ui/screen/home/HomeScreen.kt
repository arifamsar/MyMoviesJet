package com.arfsar.mymovies.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arfsar.mymovies.R
import com.arfsar.mymovies.di.Injection
import com.arfsar.mymovies.model.FavoriteFilm
import com.arfsar.mymovies.ui.ViewModelFactory
import com.arfsar.mymovies.ui.common.UiState
import com.arfsar.mymovies.ui.components.MovieItem

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
) {
    val query by viewModel.query

    Column(
        modifier = Modifier
    ) {
        SearchMovie(
            query = query,
            onQueryChange = viewModel::search,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
        viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    viewModel.getAllFilm()
                }

                is UiState.Success -> {
                    HomeContent(
                        favoriteFilm = uiState.data,
                        modifier = modifier,
                        navigateToDetail = navigateToDetail,
                        viewModel = viewModel
                    )
                }
                is UiState.Error -> {}
            }
        }
    }
}

@Composable
fun HomeContent(
    favoriteFilm: List<FavoriteFilm>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    viewModel: HomeViewModel,
) {
    val updatedFavoriteFilm by rememberUpdatedState(newValue = favoriteFilm)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.testTag("FilmList")
    ) {
        items(updatedFavoriteFilm, key = { it.film.id }) { data ->
            MovieItem(
                image = data.film.image,
                title = data.film.title,
                genre = data.film.genre,
                synopsis = data.film.synopsis,
                isFavorite = data.isFavoriteFilm,
                favoriteToggle = {
                    viewModel.toggleFavorite(data)
                },
                modifier = Modifier.clickable {
                    navigateToDetail(data.film.id)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMovie(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { },
        active = false,
        onActiveChange = { },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = { },
        placeholder = {
            Text(text = stringResource(id = R.string.search_film))
        },
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    ) {
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navigateToDetail = {}
    )
}