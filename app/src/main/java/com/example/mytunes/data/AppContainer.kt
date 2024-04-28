package com.example.mytunes.data

import android.content.Context
import com.example.mytunes.network.MyTunesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

interface AppContainer {
    val myTunesDataRepository: MyTunesDataRepository
}

class DefaultAppContainer(private val context: Context, ): AppContainer {

    private val baseUrl = "https://saavn.dev"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: MyTunesApiService by lazy {
        retrofit.create(MyTunesApiService::class.java)
    }

    override val myTunesDataRepository: MyTunesDataRepository by lazy {
        NetworkDataRepository(retrofitService)
    }


}
