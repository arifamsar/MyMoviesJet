package com.arfsar.mymovies

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.arfsar.mymovies.model.FakeFilmDataSource
import com.arfsar.mymovies.ui.navigation.Screen
import com.arfsar.mymovies.ui.theme.MyMoviesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesAppTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MyMoviesTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                MoviesApp(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_clickItem_navigateToDetailWithData() {
        composeTestRule.onNodeWithTag("FilmList").performScrollToIndex(6)
        composeTestRule.onNodeWithText(FakeFilmDataSource.dummyFilm[6].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailFilm.route)
        composeTestRule.onNodeWithText(FakeFilmDataSource.dummyFilm[6].title).assertIsDisplayed()
    }

    @Test
    fun navHost_bottomNavigationWorking() {
        composeTestRule.onNodeWithStringId(R.string.favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithStringId(R.string.profile).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithStringId(R.string.home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_clickItem_navigatesBack() {
        composeTestRule.onNodeWithTag("FilmList").performScrollToIndex(6)
        composeTestRule.onNodeWithText(FakeFilmDataSource.dummyFilm[6].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailFilm.route)
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back)).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun removeFavorite() {
        composeTestRule.onNodeWithTag("FilmList").performScrollToIndex(6)
        composeTestRule.onNodeWithText(FakeFilmDataSource.dummyFilm[6].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailFilm.route)
        composeTestRule.onNodeWithContentDescription("Favorite Button").performClick()
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back)).performClick()
        composeTestRule.onNodeWithStringId(R.string.favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithTag("favoriteToggle").performClick()
        composeTestRule.onNodeWithTag("FilmList").assertDoesNotExist()

    }
}