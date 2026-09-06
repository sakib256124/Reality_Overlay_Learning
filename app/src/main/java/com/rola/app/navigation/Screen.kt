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
    data object QuantumAI : Screen("quantum_ai")
    data object ASICore : Screen("asi_core")
    data object DigitalEducationSociety : Screen("digital_education_society")
    data object AIMetaverse : Screen("ai_metaverse")
    data object AIInfrastructure : Screen("ai_infrastructure")
    data object AIEducationOS : Screen("ai_education_os")
    data object DigitalCompanion : Screen("digital_companion")
    data object CollectiveAI : Screen("collective_ai")
    data object EducationSingularity : Screen("education_singularity")
    data object AICivilization : Screen("ai_civilization")
    data object LifelongMemory : Screen("lifelong_memory")
    data object PredictiveAI : Screen("predictive_ai")
    data object EmotionalAI : Screen("emotional_ai")
    data object CreativeAI : Screen("creative_ai")
    data object AIResearchScientist : Screen("ai_research_scientist")
    data object KnowledgeEngineering : Screen("knowledge_engineering")
    data object ReasoningAI : Screen("reasoning_ai")
    data object PlanningAI : Screen("planning_ai")
    data object MasteryAI : Screen("mastery_ai")
    data object PersonalAgent : Screen("personal_agent")
    data object EducationOrchestration : Screen("education_orchestration")
    data object SelfEvolvingAI : Screen("self_evolving_ai")
    data object DigitalTwinAI : Screen("digital_twin_ai")
    data object SpatialComputingAI : Screen("spatial_computing_ai")
    data object VirtualCampusAI : Screen("virtual_campus_ai")
    data object NeuralKnowledgeAI : Screen("neural_knowledge_ai")
    data object KnowledgeDiscoveryAI : Screen("knowledge_discovery_ai")
    data object EducationMarketplaceAI : Screen("education_marketplace_ai")
    data object GlobalEducationNetworkAI : Screen("global_education_network_ai")
    data object EducationEconomyAI : Screen("education_economy_ai")
    data object History : Screen("history")
    data object Profile : Screen("profile")
}
