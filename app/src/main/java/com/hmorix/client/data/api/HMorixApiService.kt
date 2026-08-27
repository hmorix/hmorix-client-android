package com.hmorix.client.data.api

import com.hmorix.client.HMorixApp
import com.hmorix.client.data.model.*
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface HMorixApiService {

    @POST("auth/signin")
    suspend fun signIn(@Body body: Map<String, String>): Response<ApiResponse<AuthResponse>>

    @POST("auth/signup")
    suspend fun signUp(@Body body: Map<String, String>): Response<ApiResponse<AuthResponse>>

    @POST("auth/signout")
    suspend fun signOut(): Response<ApiResponse<Unit>>

    @GET("portal")
    suspend fun getPortalData(): Response<ApiResponse<PortalData>>

    @GET("tickets")
    suspend fun getTickets(): Response<ApiResponse<List<Ticket>>>

    @POST("tickets")
    suspend fun createTicket(@Body request: CreateTicketRequest): Response<ApiResponse<Ticket>>

    @GET("invoices")
    suspend fun getInvoices(): Response<ApiResponse<List<Invoice>>>

    @POST("ai/chat")
    suspend fun sendAIMessage(@Body body: Map<String, String>): Response<ApiResponse<Map<String, String>>>

    companion object {
        fun create(): HMorixApiService {
            val sessionManager = HMorixApp.instance.sessionManager
            val baseUrl = sessionManager.apiBaseUrl.let {
                if (it.endsWith("/")) it else "$it/"
            }

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            // Simple cookie jar to persist session cookie between requests
            val cookieJar = object : CookieJar {
                private val cookieStore = mutableListOf<Cookie>()

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieStore.addAll(cookies)
                    val session = cookies.firstOrNull { it.name == "hm_session" }
                    if (session != null) {
                        sessionManager.sessionCookie = session.value
                    }
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val list = cookieStore.toMutableList()
                    val saved = sessionManager.sessionCookie
                    if (saved != null && list.none { it.name == "hm_session" }) {
                        val cookie = Cookie.Builder()
                            .domain(url.host)
                            .name("hm_session")
                            .value(saved)
                            .build()
                        list.add(cookie)
                    }
                    return list
                }
            }

            val okHttpClient = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("User-Agent", "HMorixClient-Android/1.0.0")

                    val token = sessionManager.authToken
                    if (!token.isNullOrEmpty()) {
                        requestBuilder.header("Authorization", "Bearer $token")
                    }

                    chain.proceed(requestBuilder.build())
                }
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HMorixApiService::class.java)
        }
    }
}
