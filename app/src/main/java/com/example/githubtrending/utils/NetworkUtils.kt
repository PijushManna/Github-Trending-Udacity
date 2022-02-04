package com.example.githubtrending.utils

import android.net.Uri
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

object NetworkUtils {
    private const val GITHUB_BASE_URL = "https://api.github.com/search/repositories"
    private const val PARAM_QUERY = "q"
    private const val PARAM_SORT = "sort"
    private const val sortBy = "stars"

    /**
     * Build the URL to query Github
     * @param githubSearchQuery Input text for query
     * @return valid HTTP URL
     * */
    fun buildUrl(githubSearchQuery:String): URL?{
        val buildUri = Uri.parse(GITHUB_BASE_URL)
            .buildUpon()
            .appendQueryParameter(PARAM_QUERY,githubSearchQuery)
            .appendQueryParameter(PARAM_SORT, sortBy)
            .build()
        var url:URL? = null
        try {
            url = URL(buildUri.toString())
        }catch (e:MalformedURLException){
            e.printStackTrace()
        }
        return url
    }

    /**
     * This method returns the entire response from HTTP URL
     * */
    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url:URL):String?{
        val urlConnection = url.openConnection() as HttpURLConnection
        return try {
            val urlInputStream = urlConnection.inputStream
            val inputScanner = Scanner(urlInputStream)
            inputScanner.useDelimiter("\\A")
            if (inputScanner.hasNext()){
                inputScanner.next()
            }else{
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}