package com.example.githubtrending

import android.accounts.NetworkErrorException
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.githubtrending.utils.NetworkUtils
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    companion object{
        private lateinit var mSearchBoxEditText:EditText
        private lateinit var mUrlDisplayTextView: TextView
        private lateinit var mSearchResults:TextView
        private lateinit var requestUrl:URL
        private lateinit var mProgressBar: ProgressBar
        private lateinit var mErrorMessage: TextView
        private const val TAG = "MainActivity"

        fun loadingDataInBackground(){
            mProgressBar.visibility = View.VISIBLE
            mErrorMessage.visibility = View.INVISIBLE
            mSearchResults.visibility = View.INVISIBLE
        }

        fun successfullyFetchedData(){
            mProgressBar.visibility = View.INVISIBLE
            mErrorMessage.visibility = View.INVISIBLE
            mSearchResults.visibility = View.VISIBLE
        }

        fun errorFetchingData(){
            mProgressBar.visibility = View.INVISIBLE
            mErrorMessage.visibility = View.VISIBLE
            mSearchResults.visibility = View.INVISIBLE
        }

        fun postReturnedData(result: String?){
            val mockData = """
                {
   "temp": {
      "min":11.34,
      "max":19.01
   },
   "weather": {
      "id":801,
      "condition":"Clouds",
      "description":"few clouds"
   },
   "pressure":1023.51,
   "humidity":87
}
               """
            mSearchResults.text = getWeatherCondition(mockData)
        }

        private fun getWeatherCondition(result: String?): String {
            try {
                val forecast = JSONObject(result!!)
                val weather = forecast.getJSONObject("weather")
                Log.d(TAG, weather.getString("condition"))
            }catch (e:Exception){
                e.printStackTrace()
            }
            return ""
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSearchBoxEditText = findViewById(R.id.et_search_box)
        mUrlDisplayTextView = findViewById(R.id.tv_url_display)
        mSearchResults = findViewById(R.id.tv_github_search_results_json)
        mProgressBar = findViewById(R.id.pbr_loading_data_in_background)
        mErrorMessage = findViewById(R.id.tv_error_message)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuItemThatWasSelected = item.itemId
        if (menuItemThatWasSelected == R.id.app_bar_search){
            requestUrl = NetworkUtils.buildUrl(mSearchBoxEditText.text.toString())!!
            mUrlDisplayTextView.text = requestUrl.toString()
            GithubQueryTask().execute(requestUrl)
        }
        return true
    }

    class GithubQueryTask : AsyncTask<URL,Void,String?>(){
        override fun onPreExecute() {
            loadingDataInBackground()
        }
        override fun doInBackground(vararg p0: URL): String? {
            return try{
                NetworkUtils.getResponseFromHttpUrl(p0[0])
            }catch (e:Exception){
                e.localizedMessage
            }
        }

        override fun onPostExecute(result: String?) {
            if (result.isNullOrBlank()){
                errorFetchingData()
            }else{
                successfullyFetchedData()
                 postReturnedData(result)
            }
        }
    }
}

