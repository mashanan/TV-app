package com.example.tv_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tv_app.data.repository.AuthRepositoryImpl
import com.example.tv_app.ui.auth.SignInScreen
import com.example.tv_app.ui.auth.SignUpScreen
import com.example.tv_app.ui.auth.VerifyEmailScreen
import com.example.tv_app.ui.detail.ShowDetailScreen
import com.example.tv_app.ui.list.ShowListScreen
import com.example.tv_app.ui.onboarding.OnboardingOneScreen
import com.example.tv_app.ui.onboarding.OnboardingThreeScreen
import com.example.tv_app.ui.onboarding.OnboardingTwoScreen
import com.example.tv_app.ui.splash.SplashScreen
import kotlinx.coroutines.launch

private object Routes {
    const val Splash = "splash"
    const val Onboarding1 = "onboarding1"
    const val Onboarding2 = "onboarding2"
    const val Onboarding3 = "onboarding3"
    const val SignIn = "sign_in"
    const val SignUp = "sign_up"
    const val VerifyEmail = "verify_email"
    const val List = "list"
    const val Detail = "detail/{showId}"
    fun detail(id: Int) = "detail/$id"
}

/**
 * Navigates to [route] and clears the entire current back stack first, using whatever
 * entry is currently at the bottom of the live back stack (guaranteed to be present, unlike
 * a fixed/assumed start-destination id which silently fails to pop once it has already been
 * removed from the stack by an earlier clearing navigation).
 */
private fun NavController.navigateClearingBackStack(route: String) {
    val rootId = currentBackStack.value.firstOrNull()?.destination?.id
    navigate(route) {
        if (rootId != null) {
            popUpTo(rootId) { inclusive = true }
        }
        launchSingleTop = true
    }
}

@Composable
fun TvAppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            val scope = rememberCoroutineScope()
            val authRepository = remember { AuthRepositoryImpl() }
            SplashScreen(
                onTimeout = {
                    scope.launch {
                        val destination = if (authRepository.isSignedInAndVerified()) {
                            Routes.List
                        } else {
                            Routes.Onboarding1
                        }
                        navController.navigateClearingBackStack(destination)
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
                onSignInSuccess = { navController.navigateClearingBackStack(Routes.List) },
                onNavigateToVerifyEmail = { navController.navigateClearingBackStack(Routes.VerifyEmail) },
                onNavigateToSignUp = {
                    navController.navigate(Routes.SignUp) {
                        popUpTo(Routes.SignIn) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.SignUp) {
            SignUpScreen(
                onSignUpSuccess = { navController.navigateClearingBackStack(Routes.VerifyEmail) },
                onNavigateToSignIn = {
                    navController.navigate(Routes.SignIn) {
                        popUpTo(Routes.SignUp) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.VerifyEmail) {
            VerifyEmailScreen(
                onVerified = { navController.navigateClearingBackStack(Routes.List) },
                onBackToSignIn = { navController.navigateClearingBackStack(Routes.SignIn) }
            )
        }
        composable(Routes.List) {
            val scope = rememberCoroutineScope()
            val authRepository = remember { AuthRepositoryImpl() }
            ShowListScreen(
                onShowClick = { id -> navController.navigate(Routes.detail(id)) },
                onSignOut = {
                    scope.launch {
                        authRepository.signOut()
                        navController.navigateClearingBackStack(Routes.SignIn)
                    }
                }
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
