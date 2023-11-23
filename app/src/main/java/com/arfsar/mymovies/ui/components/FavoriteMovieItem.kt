package com.arfsar.mymovies.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arfsar.mymovies.R
import com.arfsar.mymovies.ui.theme.MyMoviesTheme

@Composable
fun FavoriteMovieItem(
    image: Int,
    title: String,
    genre: String,
    synopsis: String,
    isFavorite: Boolean,
    favoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .size(width = 170.dp, height = 350.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Column(
            modifier = modifier
                .weight(1f)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.wrapContentSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "Genre: $genre",
                        fontSize = 10.sp,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }

                Text(
                    text = synopsis,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(top = 4.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                FavoriteToggleButton(
                    isFavorite = isFavorite,
                    favoriteToggle = favoriteToggle,
                    modifier = Modifier
                        .size(28.dp)
                )
            }
        }
    }
}

@Composable
fun FavoriteToggleButton(
    isFavorite: Boolean,
    favoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
    colors: IconToggleButtonColors = IconButtonDefaults.iconToggleButtonColors()
) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            favoriteToggle()
        },
        modifier = modifier
            .testTag("favoriteToggle"),
        colors = colors
    ) {
        if (isFavorite) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteMovieItemPreview() {
    MyMoviesTheme {
        FavoriteMovieItem(
            image = R.drawable.oppenheimer,
            title = "Oppenheimer",
            genre = "History",
            synopsis = "lorem ipsum dolor sit amet",
            isFavorite = false,
            favoriteToggle = {

            }
        )
    }
}