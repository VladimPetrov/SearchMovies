package ru.gb.searchmovies.data.details

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.gb.searchmovies.BuildConfig

class RemoteDataSource {
    fun getWeatherDetails(requestLink: String, callback: Callback){
        val builder: Request.Builder = Request.Builder().apply {
            url(requestLink+"?api_key=${BuildConfig.API_KEY}&language=ru-RU")
        }
        OkHttpClient().newCall(builder.build()).enqueue(callback)

    }
}