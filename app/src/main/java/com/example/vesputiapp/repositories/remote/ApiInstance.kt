package com.example.vesputiapp.repositories.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiInstance {
    companion object Factory {
        private  const val BASE_URL = "https://midgard.netzmap.com/"
        private const val TIMEOUT_SERVICE_SECONDS = 15

        fun create(): ApiClient {

            val gsonBuilder = GsonBuilder()

            val retrofit = Retrofit.Builder()
                .client(createOkHttpClientBuilder())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
            return retrofit.create(ApiClient::class.java)
        }

        private fun createOkHttpClientBuilder(): OkHttpClient {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()

            return builder
                .connectTimeout(TIMEOUT_SERVICE_SECONDS.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SERVICE_SECONDS.toLong(), TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build()
        }
    }
}