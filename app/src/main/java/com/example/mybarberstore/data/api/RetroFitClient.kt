package com.example.mybarberstore.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // 1. PRIMERO: Declaramos la variable del token
    var authToken: String? = null

    // 2. SEGUNDO: Definimos el interceptor
    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()

        // Si tenemos un token, lo añadimos a la cabecera
        authToken?.let {
            request.addHeader("Authorization", "Bearer $it")
        }

        chain.proceed(request.build())
    }

    // 3. TERCERO: Creamos el cliente de OKHttp usando el interceptor ya definido arriba
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // 4. POR ÚLTIMO: La instancia de Retrofit
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}