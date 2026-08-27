package com.hmorix.client.data.repository

import com.hmorix.client.HMorixApp
import com.hmorix.client.data.api.HMorixApiService
import com.hmorix.client.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PortalRepository(
    private val api: HMorixApiService = HMorixApiService.create()
) {
    private val sessionManager = HMorixApp.instance.sessionManager

    suspend fun signIn(email: String, pass: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = api.signIn(mapOf("email" to email, "password" to pass))
            if (response.isSuccessful && response.body()?.data != null) {
                val auth = response.body()!!.data!!
                if (!auth.token.isNullOrEmpty()) {
                    sessionManager.authToken = auth.token
                }
                sessionManager.currentUser = auth.user
                Result.success(auth.user)
            } else {
                val errorMsg = response.body()?.error ?: response.errorBody()?.string() ?: "Login failed"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(name: String, email: String, pass: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = api.signUp(mapOf("name" to name, "email" to email, "password" to pass))
            if (response.isSuccessful && response.body()?.data != null) {
                val auth = response.body()!!.data!!
                if (!auth.token.isNullOrEmpty()) {
                    sessionManager.authToken = auth.token
                }
                sessionManager.currentUser = auth.user
                Result.success(auth.user)
            } else {
                val errorMsg = response.body()?.error ?: "Sign up failed"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchPortalData(): Result<PortalData> = withContext(Dispatchers.IO) {
        try {
            val response = api.getPortalData()
            if (response.isSuccessful && response.body()?.data != null) {
                val portal = response.body()!!.data!!
                if (portal.user != null) {
                    sessionManager.currentUser = portal.user
                }
                Result.success(portal)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to fetch portal data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitTicket(subject: String, description: String, priority: String): Result<Ticket> = withContext(Dispatchers.IO) {
        try {
            val response = api.createTicket(CreateTicketRequest(subject = subject, description = description, priority = priority))
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.error ?: "Failed to submit ticket"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendAIMessage(prompt: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = api.sendAIMessage(mapOf("message" to prompt))
            if (response.isSuccessful && response.body()?.data != null) {
                val reply = response.body()!!.data!!["reply"] ?: response.body()!!.data!!["message"] ?: "No response"
                Result.success(reply)
            } else {
                Result.failure(Exception("AI response unavailable"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        sessionManager.clearSession()
    }
}
