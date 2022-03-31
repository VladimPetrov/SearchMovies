package ru.gb.searchmovies.data

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.gb.searchmovies.BuildConfig
import ru.gb.searchmovies.data.dto.MovieDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


class MovieLoader(private val listener: MovieLoaderListener, private val idMovie: Int) {
    interface MovieLoaderListener {
            fun onLoaded(movieDTO: MovieDTO)
            fun onFailed(throwable: Throwable)
        }
    @RequiresApi(Build.VERSION_CODES.N)
    fun loadMovie() {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${idMovie}?api_key=${BuildConfig.API_KEY}&language=ru-RU")
            val handler = Handler()
            Thread(Runnable {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.readTimeout = 10000
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val movieDTO: MovieDTO =
                       Gson().fromJson(getLines(bufferedReader), MovieDTO::class.java)

                    handler.post { listener.onLoaded(movieDTO) }
                } catch (e: Exception) {
                    Log.e("TTTTT", "Fail connection", e)
                    e.printStackTrace()
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            listener.onFailed(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        val str : String = reader.lines().collect(Collectors.joining("\n"))
        Log.e("EEEE5", str)
        return str
    }
}