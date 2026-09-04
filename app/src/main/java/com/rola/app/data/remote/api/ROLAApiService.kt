package com.rola.app.data.remote.api

import com.rola.app.data.remote.dto.ObjectDto
import com.rola.app.domain.model.CloudAnalyticsEvent
import com.rola.app.domain.model.CloudRecommendationResponse
import com.rola.app.domain.model.CloudTranslationRequest
import com.rola.app.domain.model.CloudTranslationResponse
import com.rola.app.domain.model.CloudTutorRequest
import com.rola.app.domain.model.CloudTutorResponse
import com.rola.app.domain.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ROLAApiService {
    @GET("objects")
    suspend fun getLearningObjects(): List<ObjectDto>

    @GET("objects/{id}")
    suspend fun getLearningObject(@Path("id") objectId: String): ObjectDto

    @GET("search")
    suspend fun searchObjects(@Query("q") query: String): List<ObjectDto>

    @GET("users")
    suspend fun getCurrentUser(): User

    @PUT("users")
    suspend fun updateCurrentUser(@Body user: User)

    @POST("chat")
    suspend fun requestTutorResponse(@Body request: CloudTutorRequest): CloudTutorResponse

    @POST("translate")
    suspend fun translate(@Body request: CloudTranslationRequest): CloudTranslationResponse

    @GET("recommendations")
    suspend fun getRecommendations(): CloudRecommendationResponse

    @POST("recommendations")
    suspend fun refreshRecommendations(): CloudRecommendationResponse

    @POST("analytics")
    suspend fun recordAnalytics(@Body event: CloudAnalyticsEvent)
}
