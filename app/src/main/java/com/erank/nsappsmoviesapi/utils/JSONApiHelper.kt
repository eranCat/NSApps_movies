package com.erank.nsappsmoviesapi.utils

import com.erank.nsappsmoviesapi.models.Movie
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.lang.Exception

object JSONApiHelper  {

    private const val apiPath = "https://api.androidhive.info/json/movies.json"

    fun loadMoviesFromJson(listener: DataSourceListener,path: String = apiPath){

        val url = Request.Builder().url(path).build()
        val client = OkHttpClient()

        client.newCall(url).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("loaded movies")
                val body = response.body?.string()
                val moviesRes = GsonBuilder().create().fromJson(body, Array<Movie>::class.java).toMutableList()

                listener.onMoviesLoaded(null, moviesRes)
            }
            override fun onFailure(call: Call, e: IOException) {
                println("failed downloading")
                listener.onMoviesLoaded(e, emptyList())
            }
        })
    }

    fun convertMovieFromJson(json: String): Movie? = Gson().fromJson(json,Movie::class.java)
}

interface DataSourceListener{
    fun onMoviesLoaded(exception: Exception?,movies:List<Movie>)
}