package ru.gb.searchmovies

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.gb.searchmovies.data.MovieDTO
import ru.gb.searchmovies.view.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val ID_MOVIE_EXTRA = "ID MOVIE"

class DetailsMoviesService(name: String = "DetailsMoviesService") : IntentService(name) {
    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val idMovie = intent.getStringExtra(ID_MOVIE_EXTRA)
            if (idMovie == null) {
                onEmptyData()
            } else {
                loadMovie(idMovie)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadMovie(idMovie: String) {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${idMovie}?api_key=${BuildConfig.API_KEY}&language=ru-RU")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlConnection.inputStream))
                val movieDTO: MovieDTO =
                    Gson().fromJson(getLines(bufferedReader), MovieDTO::class.java)
                onResponse(movieDTO)
            } catch (e: Exception) {
                Log.e("TTTTT", "Fail connection", e)
                e.printStackTrace()
            } finally {
                urlConnection.disconnect()
            }

        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        val str: String = reader.lines().collect(Collectors.joining("\n"))
        Log.e("EEEE5", str)
        return str
    }

    private fun onResponse(movieDTO: MovieDTO) {
        val fact = movieDTO
        if (fact == null) {
            onEmptyResponse()
        } else {
            onSuccessResponse(fact)
        }
    }

    private fun onSuccessResponse(
        movie: MovieDTO
    ) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_NAME_EXTRA, movie.name)
        broadcastIntent.putExtra(DETAILS_GENRES_EXTRA, movie.showGenres())
        broadcastIntent.putExtra(DETAILS_RUNTIME_EXTRA, movie.runtime)
        broadcastIntent.putExtra(DETAILS_POPULARITY_EXTRA, movie.popularity)
        broadcastIntent.putExtra(DETAILS_OVERVIEW_EXTRA, movie.overview)
        sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        sendBroadcast(broadcastIntent)
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }
}