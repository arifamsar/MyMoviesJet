package com.arfsar.mymovies.ui.screen.detail

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arfsar.mymovies.R
import com.arfsar.mymovies.di.Injection
import com.arfsar.mymovies.ui.ViewModelFactory
import com.arfsar.mymovies.ui.common.UiState
import com.arfsar.mymovies.ui.theme.MyMoviesTheme

@Composable
fun DetailScreen(
    filmId: Long,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    navigateBack: () -> Unit,
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getFilmById(filmId)
            }

            is UiState.Success -> {
                val data = uiState.data
                DetailContent(
                    image = data.film.image,
                    title = data.film.title,
                    genre = data.film.genre,
                    synopsis = data.film.synopsis,
                    isFavorite = data.isFavoriteFilm,
                    favoriteToggle = { viewModel.setFavoriteFilm(data) },
                    onBackClick = navigateBack
                )
            }

            is UiState.Error -> {

            }
        }
    }

}

@Composable
fun DetailContent(
    image: Int,
    title: String,
    genre: String,
    synopsis: String,
    isFavorite: Boolean,
    favoriteToggle: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavoriteState by remember { mutableStateOf(isFavorite) }

    Column(modifier = modifier) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    modifier = Modifier
                        .padding(16.dp)
                        .size(24.dp)
                        .clickable { onBackClick() }
                )
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = image),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .size(width = 150.dp, height = 210.dp)

                            )
                            Column(
                                modifier = modifier
                                    .padding(start = 8.dp)
                            ) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 18.sp,
                                    maxLines = 2
                                )
                                Text(text = "Genre: $genre", fontSize = 12.sp)
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Synopsis :",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = synopsis,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End
                    ) {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = if (isFavoriteState) stringResource(id = R.string.remove_favorite) else stringResource(id = R.string.add_favorite),
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isFavoriteState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                )
                            },
                            onClick = {
                                isFavoriteState = !isFavoriteState
                                favoriteToggle()
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .semantics(mergeDescendants = true) {
                                    contentDescription = "Favorite Button"
                                }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    MyMoviesTheme {
        DetailContent(
            image = R.drawable.oppenheimer,
            title = "Oppenheimer",
            genre = "Drama",
            synopsis = "The life story of the theoretical physicist who became the world's most famous scientist.",
            isFavorite = true,
            onBackClick = { },
            favoriteToggle = { })
    }
}