package com.example.mytunes.data

import android.content.Context
import com.example.mytunes.model.HomeApiResponse
import com.example.mytunes.network.MyTunesApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.StringReader
import java.lang.reflect.Type
import okhttp3.logging.HttpLoggingInterceptor
import org.intellij.lang.annotations.Language


interface AppContainer {
    val myTunesDataRepository: MyTunesDataRepository
    val myTunesDataRepository2: MyTunesDataRepository
}

class DefaultAppContainer(private val context: Context ): AppContainer {
    private val baseUrl = "https://jiosavan-api-with-playlist.vercel.app/"
    private val homeBaseUrl = "https://jio-savan-api-sigma.vercel.app/"


    private val client = createOkHttpClient()


    fun fetchAsynchronous(language: String) {
        val request = Request.Builder()
            .url("https://jio-savan-api-sigma.vercel.app/modules?language=$language")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                println("Network request failed: ${e.message}")

            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val newData = parseTodoJson(json = response.toString())
                    println("response for id $language: $responseData")
                } else {
                    println("Response failed for id $language: ${response.code}")
                    println("Response body: ${response.body?.string()}")
                }
            }
        })
    }



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

fun parseTodoJson(json: String): HomeApiResponse {
    return Gson().fromJson(json, HomeApiResponse::class.java)
}



