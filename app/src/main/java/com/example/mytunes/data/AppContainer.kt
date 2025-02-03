package com.example.mytunes.data

import android.content.Context
import com.example.mytunes.network.MyTunesApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor


interface AppContainer {
    val myTunesDataRepository: MyTunesDataRepository
    val myTunesDataRepository2: MyTunesDataRepository
}

class DefaultAppContainer(): AppContainer {
    private val baseUrl = "https://jiosavan-api-with-playlist.vercel.app/"
    private val homeBaseUrl = "https://jio-savan-api-sigma.vercel.app/"


    private val client = createOkHttpClient()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofit2 = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(homeBaseUrl)
        .build()




    private val retrofitService: MyTunesApiService by lazy {
        retrofit.create(MyTunesApiService::class.java)
    }
    private val homeRetroFitService: MyTunesApiService by lazy {
        retrofit2.create(MyTunesApiService::class.java)
    }

    override val myTunesDataRepository: MyTunesDataRepository by lazy {
        NetworkDataRepository(retrofitService)
    }

    override val myTunesDataRepository2: MyTunesDataRepository by lazy {
        NetworkDataRepository(homeRetroFitService)
    }
}

fun createOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
}




