package com.rola.app.spatial_ai.environment

import com.rola.app.domain.model.SpatialCoordinate
import com.rola.app.domain.model.SpatialEnvironmentMap
import com.rola.app.domain.model.SpatialObjectPosition
import com.rola.app.domain.model.SpatialSurface
import com.rola.app.domain.model.SpatialSurfaceType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnvironmentUnderstanding @Inject constructor() {
    fun analyzeRoom(
        detectedLabels: List<String>,
        depthQuality: Float,
    ): SpatialEnvironmentMap {
        val surfaces = listOf(
            SpatialSurface("surface-floor-${UUID.randomUUID()}", SpatialSurfaceType.Floor, SpatialCoordinate(0f, 0f, 0f), 4f, 4f),
            SpatialSurface("surface-wall-${UUID.randomUUID()}", SpatialSurfaceType.Wall, SpatialCoordinate(0f, 1.5f, -2f), 4f, 3f),
            SpatialSurface("surface-table-${UUID.randomUUID()}", SpatialSurfaceType.Table, SpatialCoordinate(0f, 0.75f, -1f), 1.8f, 0.9f),
        )
        val positions = detectedLabels.distinct().take(8).mapIndexed { index, label ->
            SpatialObjectPosition(
                objectId = "spatial-object-${label.slug()}-$index",
                label = label,
                coordinate = SpatialCoordinate(x = -0.8f + index * 0.35f, y = 0.9f, z = -1.2f - index * 0.1f),
                distanceMeters = 1.2f + index * 0.25f,
            )
        }
        return SpatialEnvironmentMap(
            mapId = "environment-map-${UUID.randomUUID()}",
            roomStructure = if (depthQuality >= 0.7f) "Depth-supported room map" else "Plane-estimated room map",
            surfaces = surfaces,
            objectPositions = positions,
            relationships = positions.zipWithNext { first, second -> "${first.label} is near ${second.label}." },
            depthQuality = depthQuality.coerceIn(0f, 1f),
        )
    }

    private fun String.slug(): String = lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
}
