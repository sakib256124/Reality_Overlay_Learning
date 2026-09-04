package com.rola.app.data.cloud

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class FirebaseAuthInterceptor @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            firebaseAuth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
        }

        val request = if (token.isNullOrBlank()) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }

        return chain.proceed(request)
    }
}
