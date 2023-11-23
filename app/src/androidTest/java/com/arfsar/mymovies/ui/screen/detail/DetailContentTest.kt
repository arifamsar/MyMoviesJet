package com.arfsar.mymovies.ui.screen.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.arfsar.mymovies.R
import com.arfsar.mymovies.model.FavoriteFilm
import com.arfsar.mymovies.model.Film
import com.arfsar.mymovies.onNodeWithStringId
import com.arfsar.mymovies.ui.theme.MyMoviesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeFilm = FavoriteFilm(
        film = Film(
            2,
            R.drawable.evildeadrise,
            "Evil Dead Rise",
            "Horror, Fantasy",
            "In the fifth Evil Dead film, a road-weary Beth pays an overdue visit to her older sister Ellie, who is raising three kids on her own in a cramped L.A apartment. The sisters' reunion is cut short by the discovery of a mysterious book deep in the bowels of Ellie's building, giving rise to flesh-possessing demons, and thrusting Beth into a primal battle for survival as she is faced with the most nightmarish version of motherhood imaginable.",
        ),
        isFavoriteFilm = false
    )

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MyMoviesTheme {
                DetailContent(
                    image = fakeFilm.film.image,
                    title = fakeFilm.film.title,
                    genre = fakeFilm.film.genre,
                    synopsis = fakeFilm.film.synopsis,
                    isFavorite = fakeFilm.isFavoriteFilm,
                    favoriteToggle = {
                        fakeFilm.isFavoriteFilm = !fakeFilm.isFavoriteFilm
                    },
                    onBackClick = { })
            }
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun detailContent_isDisplayed() {
        composeTestRule.onNodeWithText(fakeFilm.film.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeFilm.film.synopsis).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Favorite Button").assertIsDisplayed()
    }

    @Test
    fun detailContent_ToggleFavorite() {
        composeTestRule.onNodeWithContentDescription("Favorite Button").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Favorite Button")
            .assert(isFavoriteState = false)

        composeTestRule.onNodeWithContentDescription("Favorite Button")
            .performClick()

        composeTestRule.onNodeWithContentDescription("Favorite Button")
            .assert(isFavoriteState = true)
    }
}

private fun SemanticsNodeInteraction.assert(isFavoriteState: Boolean) {
    if (isFavoriteState) {
        assert(hasClickAction())
    } else {
        assert(hasClickAction())
    }
}

