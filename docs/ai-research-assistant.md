# ROLA Module 19 Autonomous AI Research Assistant

## AI Research Architecture Diagram

```text
Admin Research Dashboard
        |
        v
ResearchRepository
  - Room cache
  - Firestore sync
  - approval/version records
        |
        v
ResearchAgent
  - knowledge gap analysis
  - task generation
  - update recommendation
        |
        v
KnowledgeCollector -> ContentAnalyzer -> KnowledgeVerificationEngine
        |                    |                    |
        v                    v                    v
ScientificSource      extracted facts      reliability, duplicate,
management            relationships        consistency, moderation
        |
        v
ScientificContentGenerator
  - beginner explanation
  - advanced explanation
  - quiz prompts
  - summaries
  - flashcards
        |
        v
Human Approval
        |
        v
KnowledgeGraphRepository + Learning Materials
```

## Knowledge Update Workflow

```text
Research Task
    -> Source Collection
    -> AI Content Analysis
    -> Verification
    -> Human Approval
    -> Knowledge Graph Update
    -> Learning Content Update
    -> Version and audit record
```

## Database Structure

Local Room tables:

```text
research_tasks
scientific_sources
knowledge_updates
learning_materials
content_versions
```

Cloud Firestore collections:

```text
researchTasks
scientificSources
knowledgeUpdates
learningMaterials
contentVersions
knowledgeNodes
knowledgeRelations
```

## Kotlin Implementation

Core implementation files:

```text
app/src/main/java/com/rola/app/domain/model/ResearchTask.kt
app/src/main/java/com/rola/app/domain/model/KnowledgeUpdate.kt
app/src/main/java/com/rola/app/domain/model/ScientificSource.kt
app/src/main/java/com/rola/app/domain/model/LearningMaterial.kt
app/src/main/java/com/rola/app/data/research/ResearchAgent.kt
app/src/main/java/com/rola/app/data/research/KnowledgeCollector.kt
app/src/main/java/com/rola/app/data/research/ContentAnalyzer.kt
app/src/main/java/com/rola/app/data/research/ScientificContentGenerator.kt
app/src/main/java/com/rola/app/data/research/SourceManager.kt
app/src/main/java/com/rola/app/data/research/ResearchRepository.kt
app/src/main/java/com/rola/app/data/research/ResearchTutorBridge.kt
app/src/main/java/com/rola/app/presentation/research/ResearchDashboardScreen.kt
```

## Admin Dashboard Design

The dashboard supports:

- Pending knowledge updates.
- AI research suggestions.
- Source management seed data.
- One-step content approval and graph application.
- Knowledge statistics for tasks, reviews, trusted sources, materials, and applied updates.

The current dashboard is a standalone Compose screen so navigation can be added later without disturbing existing learner flows.

## Verification Design

Before content is approved, `KnowledgeVerificationEngine` checks:

- Source reliability.
- Duplicate risk against existing updates.
- Scientific consistency.
- Contradiction risk.
- Moderation status.

Only updates that are approved by an admin or pass verification readiness can be applied to the knowledge graph.

## AI Tutor Integration

`ResearchTutorBridge` prepares research-enhanced tutor context from approved/generated learning materials and graph context. This allows the chatbot to adopt research-expanded explanations in a later integration pass without modifying current chat behavior.

## Testing Procedure

- Unit test `ContentAnalyzer` for definitions, properties, applications, relationships, and difficulty.
- Unit test `KnowledgeVerificationEngine` for reliable source approval and duplicate/contradiction risk.
- Unit test `ScientificContentGenerator` for beginner explanations, advanced explanations, quiz prompts, summaries, and flashcards.
- Room migration test from version 8 to 9 to verify all research tables and indexes.
- Firestore rules test for admin-only writes to `researchTasks`, `knowledgeUpdates`, and `contentVersions`.
- UI smoke test for dashboard actions: analyze, create, run, approve.
- Integration test: create task -> generate update -> approve -> verify knowledge graph node/relation appears.

## Future Autonomous AI Improvement Roadmap

- Add queued background workers for scheduled research refreshes.
- Connect vetted scientific APIs and open educational datasets.
- Add contradiction detection using multiple independent sources.
- Add semantic diffing for content version review.
- Add confidence calibration from educator approval history.
- Add graph-aware retrieval augmentation for the AI tutor.
- Add admin moderation workflows with comments and rollback.
