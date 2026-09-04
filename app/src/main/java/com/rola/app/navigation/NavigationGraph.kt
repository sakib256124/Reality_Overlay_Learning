package com.rola.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rola.app.presentation.adaptive.LearningDashboard
import com.rola.app.presentation.agi_network.AGINetworkDashboardScreen
import com.rola.app.presentation.chatbot.ChatbotScreen
import com.rola.app.presentation.history.LearningHistoryScreen
import com.rola.app.presentation.neural_ai.NeuralLearningDashboardScreen
import com.rola.app.presentation.quiz.QuizScreen
import com.rola.app.presentation.scanner.ARScannerScreen
import com.rola.app.presentation.screens.HomeScreen
import com.rola.app.presentation.screens.LoginScreen
import com.rola.app.presentation.screens.ObjectDetailScreen
import com.rola.app.presentation.screens.ProfileScreen
import com.rola.app.presentation.screens.SearchScreen
import com.rola.app.presentation.screens.SplashScreen
import com.rola.app.presentation.translation.TranslationScreen
import com.rola.app.presentation.vision.VisionScannerScreen
import com.rola.app.presentation.visualization.AR3DViewerScreen
import com.rola.app.presentation.wearable.WearableDashboard

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onFinished = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(onContinue = { navController.navigate(Screen.Home.route) })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onStartArLearning = { navController.navigate(Screen.ARScanner.route) },
                onAdvancedVision = { navController.navigate(Screen.VisionScanner.route) },
                onHistory = { navController.navigate(Screen.History.route) },
                onExploreObjects = { navController.navigate(Screen.Search.route) },
                onTakeQuiz = { navController.navigate(Screen.History.route) },
                onTutor = { navController.navigate(Screen.Chatbot.createRoute()) },
                onView3D = { navController.navigate(Screen.Search.route) },
                onTranslate = { navController.navigate(Screen.Translation.createRoute()) },
                onWearable = { navController.navigate(Screen.Wearable.route) },
                onAdaptiveLearning = { navController.navigate(Screen.AdaptiveLearning.route) },
                onNeuralLearning = { navController.navigate(Screen.NeuralLearning.route) },
                onAGINetwork = { navController.navigate(Screen.AGINetwork.route) },
                onProfile = { navController.navigate(Screen.Profile.route) },
            )
        }
        composable(Screen.ARScanner.route) {
            ARScannerScreen(onBack = navController::popBackStack)
        }
        composable(Screen.VisionScanner.route) {
            VisionScannerScreen(onBack = navController::popBackStack)
        }
        composable(Screen.Search.route) {
            SearchScreen(
                onBack = navController::popBackStack,
                onObjectSelected = { objectId -> navController.navigate(Screen.ObjectDetail.createRoute(objectId)) },
            )
        }
        composable(
            route = Screen.ObjectDetail.route,
            arguments = listOf(navArgument("objectId") { type = NavType.StringType }),
        ) { backStackEntry ->
            ObjectDetailScreen(
                objectId = backStackEntry.arguments?.getString("objectId").orEmpty(),
                onBack = navController::popBackStack,
                onQuiz = { objectId -> navController.navigate(Screen.Quiz.createRoute(objectId)) },
                onTutor = { objectId -> navController.navigate(Screen.Chatbot.createRoute(objectId)) },
                onView3D = { objectId -> navController.navigate(Screen.Visualization.createRoute(objectId)) },
                onTranslate = { objectId -> navController.navigate(Screen.Translation.createRoute(objectId)) },
            )
        }
        composable(
            route = Screen.Quiz.route,
            arguments = listOf(navArgument("objectId") { type = NavType.StringType }),
        ) {
            QuizScreen(onBack = navController::popBackStack)
        }
        composable(
            route = Screen.Chatbot.route,
            arguments = listOf(navArgument("objectId") { type = NavType.StringType }),
        ) {
            ChatbotScreen(onBack = navController::popBackStack)
        }
        composable(
            route = Screen.Visualization.route,
            arguments = listOf(navArgument("objectId") { type = NavType.StringType }),
        ) {
            AR3DViewerScreen(onBack = navController::popBackStack)
        }
        composable(
            route = Screen.Translation.route,
            arguments = listOf(navArgument("objectId") { type = NavType.StringType }),
        ) {
            TranslationScreen(onBack = navController::popBackStack)
        }
        composable(Screen.Wearable.route) {
            WearableDashboard(onBack = navController::popBackStack)
        }
        composable(Screen.AdaptiveLearning.route) {
            LearningDashboard(onBack = navController::popBackStack)
        }
        composable(Screen.NeuralLearning.route) {
            NeuralLearningDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.AGINetwork.route) {
            AGINetworkDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.History.route) {
            LearningHistoryScreen(
                onBack = navController::popBackStack,
                onObjectSelected = { objectId ->
                    navController.navigate(Screen.ObjectDetail.createRoute(objectId))
                },
                onQuizSelected = { objectId ->
                    navController.navigate(Screen.Quiz.createRoute(objectId))
                },
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(onBack = navController::popBackStack)
        }
    }
}
