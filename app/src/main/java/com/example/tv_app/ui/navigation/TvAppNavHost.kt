package com.example.tv_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tv_app.ui.detail.ShowDetailScreen
import com.example.tv_app.ui.list.ShowListScreen

private object Routes {
    const val List = "list"
    const val Detail = "detail/{showId}"
    fun detail(id: Int) = "detail/$id"
}

@Composable
fun TvAppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.List) {
        composable(Routes.List) {
            ShowListScreen(
                onShowClick = { id -> navController.navigate(Routes.detail(id)) }
            )
        }
        composable(
            route = Routes.Detail,
            arguments = listOf(navArgument("showId") { type = NavType.IntType })
        ) { backStackEntry ->
            val showId = backStackEntry.arguments?.getInt("showId") ?: return@composable
            ShowDetailScreen(
                showId = showId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
