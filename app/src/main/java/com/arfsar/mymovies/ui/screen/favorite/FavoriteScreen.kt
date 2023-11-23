package com.arfsar.mymovies.ui.screen.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arfsar.mymovies.R
import com.arfsar.mymovies.di.Injection
import com.arfsar.mymovies.model.FavoriteFilm
import com.arfsar.mymovies.ui.ViewModelFactory
import com.arfsar.mymovies.ui.common.UiState
import com.arfsar.mymovies.ui.components.FavoriteMovieItem
import com.arfsar.mymovies.ui.components.MovieItem
import com.arfsar.mymovies.ui.theme.MyMoviesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
) {
    val favoriteViewModel: FavoriteViewModel = viewModel

    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.favorite_menu),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        )
        viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    viewModel.getAllFavoriteFilm()
                }
                is UiState.Success -> {
                    val favoriteFilm = uiState.data
                    if (favoriteFilm.isEmpty()) {
                        FavoriteEmptyState()
                    } else {
                        FavoriteContent(
                            favoriteFilm = uiState.data,
                            modifier = modifier,
                            navigateToDetail = navigateToDetail,
                            viewModel = favoriteViewModel
                        )
                    }
                }

                is UiState.Error -> {}
            }
        }
    }

}


@Composable
fun FavoriteContent(
    favoriteFilm: List<FavoriteFilm>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    viewModel: FavoriteViewModel
) {
    val updatedFavoriteFilms by rememberUpdatedState(newValue = favoriteFilm)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            items(updatedFavoriteFilms) { data ->
                FavoriteMovieItem(
                    image = data.film.image,
                    title = data.film.title,
                    genre = data.film.genre,
                    synopsis = data.film.synopsis,
                    isFavorite = data.isFavoriteFilm,
                    favoriteToggle = {
                        viewModel.toggleFavoriteFilm(data)

                    },
                    modifier = Modifier.clickable {
                        navigateToDetail(data.film.id)
                    }
                )
            }
        }
    }
}

@Composable
fun FavoriteEmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_favorite),
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteScreenPreview() {
    MyMoviesTheme {
        FavoriteScreen(navigateToDetail = {})
    }
}