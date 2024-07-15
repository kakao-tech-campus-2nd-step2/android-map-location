package campus.tech.kakao.map.data.network

import android.util.Log
import campus.tech.kakao.map.data.model.SearchResponse
import campus.tech.kakao.map.utils.ApiKeyProvider
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

object HttpService {
    private const val BASE_URL = "https://dapi.kakao.com/"

    fun searchKeyword(query: String, callback: (SearchResponse?) -> Unit) {
        Thread {
            val url = "${BASE_URL}v2/local/search/keyword.json?query=$query"
            val connection = URL(url).openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", ApiKeyProvider.KAKAO_REST_API_KEY)
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    callback(Gson().fromJson(response, SearchResponse::class.java))
                } else {
                    Log.d(
                        "testt",
                        "Response failed: ${connection.errorStream.bufferedReader().readText()}"
                    )
                    callback(null)
                }
            } finally {
                connection.disconnect()
            }
        }.start()
    }

}