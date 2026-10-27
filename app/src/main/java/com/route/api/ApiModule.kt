package com.route.api

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor{
        val interceptor =  HttpLoggingInterceptor { message -> Log.e("RetroFit", message) }
        interceptor.setLevel(level = HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    @Singleton
    @HeadersInterceptor
    fun provideHeadersInterceptor(
        @dagger.hilt.android.qualifiers.ApplicationContext context: android.content.Context
    ): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val prefs = context.getSharedPreferences("route_ecommerce_auth", android.content.Context.MODE_PRIVATE)
            val token = prefs.getString("auth_token", null)
            val newRequest = if (!token.isNullOrBlank() && request.header("token") == null) {
                request.newBuilder().addHeader("token", token).build()
            } else {
                request
            }
            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(
         loggingInterceptor: HttpLoggingInterceptor,
         @HeadersInterceptor headersInterceptor: Interceptor
    ): OkHttpClient{
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(headersInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
        return okhttp
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit{
        val retrofit = Retrofit.Builder()
            .client(okHttp)
            .baseUrl("https://ecommerce.routemisr.com/api/")
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build()
        return retrofit
    }

    @Provides
    @Singleton
    fun provideServices(
        retrofit: Retrofit
    ): ApiServices{
        return retrofit.create(ApiServices::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HeadersInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptor
