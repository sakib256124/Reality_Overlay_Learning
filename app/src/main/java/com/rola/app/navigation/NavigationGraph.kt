package com.rola.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rola.app.presentation.adaptive.LearningDashboard
import com.rola.app.presentation.agi_network.AGINetworkDashboardScreen
import com.rola.app.presentation.ai_infrastructure.AIInfrastructureDashboardScreen
import com.rola.app.presentation.ai_metaverse.MetaverseDashboardScreen
import com.rola.app.presentation.ai_os.AIOSDashboardScreen
import com.rola.app.presentation.ai_civilization.CivilizationDashboardScreen
import com.rola.app.presentation.ai_research.ResearchIntelligenceDashboardScreen
import com.rola.app.presentation.asi_core.ASIDashboardScreen
import com.rola.app.presentation.chatbot.ChatbotScreen
import com.rola.app.presentation.collective_ai.CollectiveAIDashboardScreen
import com.rola.app.presentation.creative_ai.CreativeAIDashboardScreen
import com.rola.app.presentation.digital_education_society.DigitalEducationSocietyDashboardScreen
import com.rola.app.presentation.digital_companion.CompanionDashboardScreen
import com.rola.app.presentation.education_singularity.SingularityDashboardScreen
import com.rola.app.presentation.education_orchestration.EducationOrchestrationDashboardScreen
import com.rola.app.presentation.emotional_ai.EmotionalLearningDashboardScreen
import com.rola.app.presentation.lifelong_memory.LifelongLearningDashboardScreen
import com.rola.app.presentation.mastery_ai.MasteryDashboardScreen
import com.rola.app.presentation.predictive_ai.PredictiveDashboardScreen
import com.rola.app.presentation.history.LearningHistoryScreen
import com.rola.app.presentation.knowledge_engineering.KnowledgeIntelligenceDashboardScreen
import com.rola.app.presentation.neural_ai.NeuralLearningDashboardScreen
import com.rola.app.presentation.personal_agent.PersonalAgentDashboardScreen
import com.rola.app.presentation.quantum_ai.QuantumAIDashboardScreen
import com.rola.app.presentation.quiz.QuizScreen
import com.rola.app.presentation.planning_ai.PlanningDashboardScreen
import com.rola.app.presentation.reasoning_ai.ReasoningDashboardScreen
import com.rola.app.presentation.scanner.ARScannerScreen
import com.rola.app.presentation.self_evolving_ai.EvolutionDashboardScreen
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
                onQuantumAI = { navController.navigate(Screen.QuantumAI.route) },
                onASICore = { navController.navigate(Screen.ASICore.route) },
                onDigitalEducationSociety = { navController.navigate(Screen.DigitalEducationSociety.route) },
                onAIMetaverse = { navController.navigate(Screen.AIMetaverse.route) },
                onAIInfrastructure = { navController.navigate(Screen.AIInfrastructure.route) },
                onAIEducationOS = { navController.navigate(Screen.AIEducationOS.route) },
                onDigitalCompanion = { navController.navigate(Screen.DigitalCompanion.route) },
                onCollectiveAI = { navController.navigate(Screen.CollectiveAI.route) },
                onEducationSingularity = { navController.navigate(Screen.EducationSingularity.route) },
                onAICivilization = { navController.navigate(Screen.AICivilization.route) },
                onLifelongMemory = { navController.navigate(Screen.LifelongMemory.route) },
                onPredictiveAI = { navController.navigate(Screen.PredictiveAI.route) },
                onEmotionalAI = { navController.navigate(Screen.EmotionalAI.route) },
                onCreativeAI = { navController.navigate(Screen.CreativeAI.route) },
                onAIResearchScientist = { navController.navigate(Screen.AIResearchScientist.route) },
                onKnowledgeEngineering = { navController.navigate(Screen.KnowledgeEngineering.route) },
                onReasoningAI = { navController.navigate(Screen.ReasoningAI.route) },
                onPlanningAI = { navController.navigate(Screen.PlanningAI.route) },
                onMasteryAI = { navController.navigate(Screen.MasteryAI.route) },
                onPersonalAgent = { navController.navigate(Screen.PersonalAgent.route) },
                onEducationOrchestration = { navController.navigate(Screen.EducationOrchestration.route) },
                onSelfEvolvingAI = { navController.navigate(Screen.SelfEvolvingAI.route) },
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
        composable(Screen.QuantumAI.route) {
            QuantumAIDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.ASICore.route) {
            ASIDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.DigitalEducationSociety.route) {
            DigitalEducationSocietyDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.AIMetaverse.route) {
            MetaverseDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.AIInfrastructure.route) {
            AIInfrastructureDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.AIEducationOS.route) {
            AIOSDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.DigitalCompanion.route) {
            CompanionDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.CollectiveAI.route) {
            CollectiveAIDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.EducationSingularity.route) {
            SingularityDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.AICivilization.route) {
            CivilizationDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.LifelongMemory.route) {
            LifelongLearningDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.PredictiveAI.route) {
            PredictiveDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.EmotionalAI.route) {
            EmotionalLearningDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.CreativeAI.route) {
            CreativeAIDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.AIResearchScientist.route) {
            ResearchIntelligenceDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.KnowledgeEngineering.route) {
            KnowledgeIntelligenceDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.ReasoningAI.route) {
            ReasoningDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.PlanningAI.route) {
            PlanningDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.MasteryAI.route) {
            MasteryDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.PersonalAgent.route) {
            PersonalAgentDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.EducationOrchestration.route) {
            EducationOrchestrationDashboardScreen(onBack = navController::popBackStack)
        }
        composable(Screen.SelfEvolvingAI.route) {
            EvolutionDashboardScreen(onBack = navController::popBackStack)
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
