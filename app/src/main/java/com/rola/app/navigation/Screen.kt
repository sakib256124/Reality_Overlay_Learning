package com.rola.app.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object ARScanner : Screen("ar_scanner")
    data object VisionScanner : Screen("vision_scanner")
    data object Search : Screen("search")
    data object ObjectDetail : Screen("object_detail/{objectId}") {
        fun createRoute(objectId: String) = "object_detail/$objectId"
    }
    data object Quiz : Screen("quiz/{objectId}") {
        fun createRoute(objectId: String) = "quiz/$objectId"
    }
    data object Chatbot : Screen("chatbot/{objectId}") {
        fun createRoute(objectId: String? = null) = "chatbot/${objectId ?: "none"}"
    }
    data object Visualization : Screen("visualization/{objectId}") {
        fun createRoute(objectId: String) = "visualization/$objectId"
    }
    data object Translation : Screen("translation/{objectId}") {
        fun createRoute(objectId: String? = null) = "translation/${objectId ?: "none"}"
    }
    data object Wearable : Screen("wearable")
    data object AdaptiveLearning : Screen("adaptive_learning")
    data object NeuralLearning : Screen("neural_learning")
    data object AGINetwork : Screen("agi_network")
    data object History : Screen("history")
    data object Profile : Screen("profile")
}
