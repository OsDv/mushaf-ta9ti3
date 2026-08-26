package com.example.mushaf_ta9ti3

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mushaf_ta9ti3.repository.QuranRepository
import com.example.mushaf_ta9ti3.ui.screens.HomeScreen
import com.example.mushaf_ta9ti3.ui.screens.MushafNavigationScreen
import com.example.mushaf_ta9ti3.ui.screens.MushafScreen
import com.example.mushaf_ta9ti3.ui.screens.SettingsScreen
import com.example.mushaf_ta9ti3.ui.screens.Ta9ti3ResultScreen
import com.example.mushaf_ta9ti3.ui.screens.Ta9ti3SessionScreen
import com.example.mushaf_ta9ti3.ui.screens.Ta9ti3SetupScreen
import com.example.mushaf_ta9ti3.view.MushafViewModel
import com.example.mushaf_ta9ti3.view.SettingsViewModel
import com.example.mushaf_ta9ti3.view.Ta9ti3ViewModel

// Define the routes as simple strings
object Routes {
    const val HOME = "home"
    const val MUSHAF_SELECTION = "mushaf_selection"
    const val MUSHAF_VIEW = "mushaf_view"
    const val TA9TI3_SETUP = "ta9ti3_setup"
    const val TA9TI3_SESSION = "ta9ti3_session"
    const val TA9TI3_RESULT = "ta9ti3_result"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavigation(viewModelFactory: AppViewModelFactory) {
    // This controller manages all screen swapping and back-button presses
    val navController = rememberNavController()
    val mushafViewModel: MushafViewModel = viewModel(factory = viewModelFactory)
    val ta9ti3ViewModel: Ta9ti3ViewModel = viewModel(factory = viewModelFactory)
    val settinsViewModel: SettingsViewModel = viewModel(factory = viewModelFactory)

    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToMushaf = { navController.navigate(Routes.MUSHAF_SELECTION) },
                onNavigateToTa9ti3 = { navController.navigate(Routes.TA9TI3_SETUP) },
                onNavigationToSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(Routes.MUSHAF_SELECTION) {
            MushafNavigationScreen(
                onSelectionMade = {
                    navController.navigate(Routes.MUSHAF_VIEW)
                },
                onBack = { navController.popBackStack() },
                viewModel = mushafViewModel
            )
        }

        composable(Routes.MUSHAF_VIEW) {
            MushafScreen(
                onReturnHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) {
                            inclusive = true
                        }
                    }
                },
                viewModel = mushafViewModel,
            )
        }

        composable(Routes.TA9TI3_SETUP) {
            Ta9ti3SetupScreen(
                onStartTest = { navController.navigate(Routes.TA9TI3_SESSION) },
                onBack = { navController.popBackStack() },
                viewModel = ta9ti3ViewModel
            )
        }

        composable(Routes.TA9TI3_SESSION) {
            Ta9ti3SessionScreen(
                onEndSession = { navController.navigate(Routes.TA9TI3_RESULT) },
                viewModel = ta9ti3ViewModel
            )
        }

        composable(Routes.TA9TI3_RESULT) {
            Ta9ti3ResultScreen(
                onReturnHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                viewModel = ta9ti3ViewModel
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onReturnHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                viewModel = settinsViewModel
            )
        }
    }
}

class AppViewModelFactory(private val repository: QuranRepository, private val userPreferences: UserPreferences) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MushafViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MushafViewModel(repository,userPreferences) as T
        }
        if (modelClass.isAssignableFrom(Ta9ti3ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return Ta9ti3ViewModel(repository,userPreferences) as T
        }
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java))  {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}