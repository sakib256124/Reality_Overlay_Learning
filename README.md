# Reality Overlay Learning App (ROLA)

ROLA is a native Android AI + AR educational platform. Learners scan real-world objects, get TensorFlow Lite recognition results, see AR information overlays, hear Text-to-Speech explanations, explore knowledge graphs, ask an AI tutor, translate learning content, complete quizzes, receive adaptive recommendations, and sync progress with Firebase-backed cloud services.

Repository: `Reality_Overlay_Learning`.

## Architecture

```text
com.rola.app
├── core
│   ├── ai
│   ├── AppException.kt
│   ├── ErrorHandler.kt
│   ├── Result.kt
│   └── SecureSessionStore.kt
├── ai_teacher
│   ├── assessment
│   ├── curriculum
│   ├── lesson
│   ├── personalization
│   └── teaching
├── data
│   ├── ar
│   ├── chatbot
│   ├── database
│   │   ├── converters
│   │   └── entities
│   ├── firestore
│   ├── ml
│   ├── model3d
│   ├── quiz
│   ├── research
│   ├── remote
│   ├── repository
│   ├── translation
│   └── voice
├── domain
│   ├── model
│   ├── repository
│   └── usecase
├── presentation
│   ├── chatbot
│   ├── ecosystem
│   ├── history
│   ├── overlay
│   ├── quiz
│   ├── recognition
│   ├── research
│   ├── ai_teacher
│   ├── scanner
│   ├── screens
│   ├── theme
│   ├── translation
│   ├── visualization
│   └── voice
├── di
└── navigation
```

## Learning Pipeline

```text
AR Scanner
↓
ARCore camera frame
↓
TensorFlow Lite recognition
↓
Knowledge lookup from Room
↓
Floating AR information overlay
↓
Text-to-Speech explanation
↓
Scan saved to local history
↓
Firestore sync when authenticated and online
↓
Quiz generation from object knowledge
↓
Quiz result saved, synced, and reflected in progress
↓
AI tutor answers grounded follow-up questions
↓
Translation layer localizes object knowledge, tutor answers, quiz text, and voice playback
↓
Knowledge graph and research assistant enrich explanations
↓
AI orchestrator coordinates agents, context, session state, analytics, and next recommendations
```

## Platform Documentation

- Final ecosystem integration: `docs/final-ai-ecosystem.md`
- Production deployment: `docs/production-deployment.md`
- Enterprise education platform: `docs/enterprise-platform.md`
- Global AI education network: `docs/global-education-network.md`
- Autonomous AI teacher and curriculum generator: `docs/ai-teacher-curriculum.md`
- CI/CD guide: `docs/cicd-guide.md`
- Maintenance guide: `docs/maintenance-guide.md`
- Google Play release guide: `docs/google-play-release.md`
- Cloud architecture: `docs/cloud-architecture.md`
- AI research assistant: `docs/ai-research-assistant.md`
- Backend deployment: `backend/DEPLOYMENT.md`
- Backend testing: `backend/TESTING.md`

## Setup

1. Install Android Studio and Android SDK API 36.
2. Add SDK configuration:

```properties
sdk.dir=/path/to/Android/Sdk
```

3. Add Firebase configuration if cloud sync is enabled:

```text
app/google-services.json
```

4. Add the TensorFlow Lite model:

```text
app/src/main/assets/ml/object_classifier.tflite
app/src/main/assets/ml/labels.txt
```

5. Build:

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

## Firebase

The app uses Firebase Auth for user identity and Firestore for cloud backup. Current collections:

```text
users/{userId}
objects/{objectId}
scanHistory/{scanId}
users/{userId}/quizResults/{resultId}
3DModels/{modelId}
translations/{languageCode}/translatedContent/{cacheId}
curriculums/{curriculumId}
lessons/{lessonId}
activities/{activityId}
assessments/{assessmentId}
teachingPlans/{planId}
teacherReviews/{reviewId}
lessonAnalytics/{analyticsId}
```

Suggested Firestore rules:

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;

      match /quizResults/{resultId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
      }
    }

    match /scanHistory/{scanId} {
      allow read, write: if request.auth != null && request.auth.uid == resource.data.userId;
      allow create: if request.auth != null && request.auth.uid == request.resource.data.userId;
    }

    match /objects/{objectId} {
      allow read: if request.auth != null;
      allow write: if false;
    }

    match /3DModels/{modelId} {
      allow read: if request.auth != null;
      allow write: if false;
    }

    match /translations/{languageCode}/translatedContent/{cacheId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null
        && request.resource.data.targetLanguage == languageCode
        && request.resource.data.sourceText is string
        && request.resource.data.translatedText is string;
      allow update, delete: if false;
    }
  }
}
```

## Database

Room database: `rola.db`, version `17`.

Tables:

- `users`: local/cloud user profile metadata.
- `learning_objects`: knowledge content used by overlays, search, details, voice, and quizzes.
- `scan_history`: object scan history with confidence and learning status.
- `quizzes`: generated offline quiz definitions.
- `quiz_results`: offline-first assessment results with sync metadata.
- `chat_messages`: local AI tutor conversation cache scoped by user and optional object.
- `languages`: supported language metadata and local model availability hints.
- `translation_cache`: SHA-256 keyed offline cache for repeated translations.
- `knowledge_nodes`, `knowledge_relations`, `learning_paths`: semantic knowledge graph and learning routes.
- `research_tasks`, `scientific_sources`, `knowledge_updates`, `learning_materials`, `content_versions`: autonomous research assistant cache, approval, and version records.
- `enterprise_roles`, `institutions`, `departments`, `courses`, `classes`, `members`, `assignments`, `submissions`, `classroom_sessions`, `teacher_analytics`, `student_reports`, `lms_connections`, `enterprise_audit_logs`: school, teacher, student, parent, LMS, and administration support.
- `ai_curriculums`, `curriculum_modules`, `lessons`, `activities`, `assessments`, `ai_teaching_plans`, `teacher_reviews`, `lesson_analytics`: autonomous teacher planning, generated lessons, teacher approval, and lesson effectiveness analytics.
- `neural_profiles`, `brain_signals`, `neural_cognitive_states`, `neural_learning_states`, `neural_interactions`, `attention_records`, `neural_learning_predictions`, `cognitive_reports`: future-ready neural learning interface, local signal analysis, explainable adaptation, and privacy-aware cognitive reporting.
- `agi_network_agents`, `agi_network_agent_tasks`, `agi_network_agent_communication`, `agi_network_ai_evolution_history`, `agi_network_knowledge_evolution`, `agi_network_ai_decisions`, `agi_network_curriculum_evolution`, `agi_network_analytics`, `agi_network_governance_records`: supervised multi-agent AGI education network, self-learning draft plans, curriculum evolution, analytics, and governance audit records.
- Global cloud collections under `globalInstitutions`, `sharedKnowledgeResources`, `marketplaceListings`, `collaborationRooms`, `communityPosts`, `globalAnalytics`, and tenant-scoped `tenants/{institutionId}` paths support multi-institution knowledge sharing.

## Neural AI Learning

Module 29 adds a future brain-computer education foundation under:

```text
app/src/main/java/com/rola/app/neural_ai/
```

It includes a simulated `BrainComputerInterface`, signal processor, cognitive-state analyzer, neural learning profile manager, prediction engine, personalization engine, optimizer, teaching agent, privacy manager, performance throttler, Room tables, and a Compose dashboard. See `docs/neural-ai-learning-interface-module-29.md`.

## AGI Education Network

Module 30 adds a self-evolving, human-governed AGI education network under:

```text
app/src/main/java/com/rola/app/agi_network/
```

It coordinates AI Teacher, Tutor, Research, Knowledge, Assessment, Analytics, Robot Teaching, and Cognitive Learning agents. The system creates draft educational decisions, knowledge evolution proposals, curriculum updates, and analytics reports while keeping publication behind governance controls. See `docs/autonomous-agi-education-network-module-30.md`.

## AI Tutor

The tutor module is offline-first and grounded by local app knowledge. It retrieves relevant object information, learning history, quiz progress, and previous messages before generating a response through `LLMService`.

Current implementation:

- Local grounded response generation.
- RAG-style object lookup from Room search and object details.
- Personalized hints from quiz average, recent scans, and learning level.
- Chat history cached in Room.
- Suggested questions.
- TTS playback of tutor answers.
- Disabled microphone affordance for future voice input.

Cloud AI integration should be added behind `LLMService` through a trusted backend. Do not ship raw LLM API keys in the Android app. Use Firebase Auth tokens or App Check to authorize requests to your backend.

## 3D Visualization

The 3D visualization module uses SceneView on top of Google Filament and ARCore. It loads GLB/GLTF metadata from Firestore or local fallback metadata, resolves cached downloaded models, then falls back to bundled assets under:

```text
app/src/main/assets/models/{objectId}.glb
```

Firestore `3DModels` documents:

```text
modelId: string
objectId: string
modelName: string
modelUrl: https URL
thumbnail: https URL
category: string
fileFormat: glb | gltf
scale: number
rotationX: number
rotationY: number
rotationZ: number
description: string
```

Keep mobile AR models lightweight: use self-contained `.glb`, keep geometry under about 50k triangles, prefer 1024px textures or lower, and test memory on low-end ARCore devices.

## Translation

The multilingual layer uses ML Kit on-device Translation and Language Identification. It supports English, Bengali, Spanish, French, German, Chinese, and Arabic through `LanguageManager`, stores repeated translations in Room, and mirrors reusable translated content to Firestore:

```text
translations/{languageCode}/translatedContent/{cacheId}
```

`TranslationRepository` exposes reusable entry points for:

- `translateObjectInformation()` for AR panels and object detail content.
- `translateTutorResponse()` for AI tutor answers.
- `translateQuizText()` for quiz prompts and explanations.
- `voiceLanguageFor()` to route translated text into the matching TTS locale.

ML Kit language models are downloaded on demand and then reused offline. For production, keep third-party cloud translation behind a trusted backend and never ship raw service keys in the Android app.

## Production Checklist

- Configure `environments/dev`, `environments/staging`, and `environments/production`.
- Configure GitHub Actions secrets for Firebase configs, release signing, Firebase deploy, and Google Play publishing.
- Add signed release config outside source control.
- Verify `google-services.json` is present for Firebase builds.
- Verify `object_classifier.tflite` exists and labels match model output.
- Verify bundled or Firestore-backed GLB/GLTF models exist for target objects.
- Verify ML Kit translation models download on first language use.
- Run unit, integration, and device tests on ARCore-supported Android 10+ devices.
- Test offline scan, quiz, and later Firestore sync.
- Test offline translation after language model download and Room cache warm-up.
- Review Firestore security rules before release.
- Keep API keys restricted in Google Cloud/Firebase console.
- Confirm release R8 rules preserve Firebase, Room entities, ARCore/SceneView, and TensorFlow Lite.
- Review `ops/monitoring-dashboard.md` after every staged rollout.

## Test Matrix

- Unit: quiz generation, quiz scoring, prompt building, local tutor responses, converters, ViewModel state.
- Integration: Room migrations, scan save, quiz result save, chat cache, translation cache, 3D model cache, Firestore sync retry.
- UI: navigation, dashboard actions, history search/filter, chatbot messages, quiz answers, translation language switching, 3D viewer controls, scanner permission states.
- Device: Android 10+, multiple camera resolutions, low-end and high-end ARCore devices.

## Future Improvements

- Cloud LLM tutor responses through a secure backend.
- AR glasses support.
- Real-time conversation mode with speech input and translated captions.
- Advanced 3D anatomy/parts hotspots.
- Multiplayer classroom mode.
