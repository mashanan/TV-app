package com.example.tv_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tv_app.ui.auth.SignInScreen
import com.example.tv_app.ui.auth.SignUpScreen
import com.example.tv_app.ui.detail.ShowDetailScreen
import com.example.tv_app.ui.list.ShowListScreen
import com.example.tv_app.ui.onboarding.OnboardingOneScreen
import com.example.tv_app.ui.onboarding.OnboardingThreeScreen
import com.example.tv_app.ui.onboarding.OnboardingTwoScreen
import com.example.tv_app.ui.splash.SplashScreen

private object Routes {
    const val Splash = "splash"
    const val Onboarding1 = "onboarding1"
    const val Onboarding2 = "onboarding2"
    const val Onboarding3 = "onboarding3"
    const val SignIn = "sign_in"
    const val SignUp = "sign_up"
    const val List = "list"
    const val Detail = "detail/{showId}"
    fun detail(id: Int) = "detail/$id"
}

@Composable
fun TvAppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Routes.Onboarding1) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Onboarding1) {
            OnboardingOneScreen(
                onEnterNow = { navController.navigate(Routes.Onboarding2) }
            )
        }
        composable(Routes.Onboarding2) {
            OnboardingTwoScreen(
                onNext = { navController.navigate(Routes.Onboarding3) }
            )
        }
        composable(Routes.Onboarding3) {
            OnboardingThreeScreen(
                onSignIn = { navController.navigate(Routes.SignIn) },
                onSignUp = { navController.navigate(Routes.SignUp) }
            )
        }
        composable(Routes.SignIn) {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigate(Routes.List) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Routes.SignUp) {
                        popUpTo(Routes.SignIn) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.SignUp) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Routes.List) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.navigate(Routes.SignIn) {
                        popUpTo(Routes.SignUp) { inclusive = true }
                    }
                }
            )
        }
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
