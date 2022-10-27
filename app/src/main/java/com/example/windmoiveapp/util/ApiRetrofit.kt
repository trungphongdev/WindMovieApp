package com.example.windmoiveapp.util

import android.content.Context
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiRetrofit {
    private fun getHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .build()
    }

    fun getRetrofitBuilder(isPassNull: Boolean?): Retrofit {
        val gson = if (isPassNull == true) Gson().apply { serializeNulls() } else Gson()
        return Retrofit.Builder().baseUrl(BASE_URL_YOUTUBE)
            .client(getHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    inline fun <reified T> getService(
        isPassNull: Boolean? = null
    ): T = getRetrofitBuilder(isPassNull).create(T::class.java)
}

