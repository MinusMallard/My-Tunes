package com.example.mytunes.data

import android.content.Context
import com.example.mytunes.network.MyTunesApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.StringReader
import java.lang.reflect.Type


interface AppContainer {
    val myTunesDataRepository: MyTunesDataRepository
    val myTunesDataRepository2: MyTunesDataRepository
}

class DefaultAppContainer(private val context: Context ): AppContainer {
    private val baseUrl = "https://jiosavan-api-with-playlist.vercel.app/"
    private val homeBaseUrl = "https://jio-savan-api-sigma.vercel.app/"



    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofit2 = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
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



