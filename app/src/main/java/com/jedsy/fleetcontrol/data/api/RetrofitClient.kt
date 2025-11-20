package com.jedsy.fleetcontrol.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit client factory for API services
 */
object RetrofitClient {
    
    private var healthcheckBaseUrl = "https://ping.uphi.cc/"
    private var monitorBaseUrl = "https://monitor.uphi.cc/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val healthcheckApi: HealthcheckApiService by lazy {
        createRetrofit(healthcheckBaseUrl).create(HealthcheckApiService::class.java)
    }
    
    /**
     * Update backend environment
     * @param isDev true for dev (uphi.cc), false for prod (uphi.ch)
     */
    fun setEnvironment(isDev: Boolean) {
        healthcheckBaseUrl = if (isDev) {
            "https://ping.uphi.cc/"
        } else {
            "https://ping.uphi.ch/"
        }
        
        monitorBaseUrl = if (isDev) {
            "https://monitor.uphi.cc/"
        } else {
            "https://monitor.uphi.ch/"
        }
    }
    
    fun getHealthcheckBaseUrl() = healthcheckBaseUrl
    fun getMonitorBaseUrl() = monitorBaseUrl
}
