package campus.tech.kakao.map.Data.Datasource.Remote

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.Data.Datasource.Remote.Response.Document
import campus.tech.kakao.map.Data.Datasource.Remote.Response.Meta
import campus.tech.kakao.map.Data.Datasource.Remote.Response.Response
import campus.tech.kakao.map.Data.Datasource.Remote.Response.SameName
import campus.tech.kakao.map.Data.Datasource.Remote.Response.SearchResponse
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class HttpUrlConnect : RemoteService{

    override fun getPlaceResponse(query: String) : List<Document>{
        val body = mutableMapOf("query" to query, "page" to START_PAGE.toString())
        val info = mapOf("method" to "GET", "type" to "Search")

        return handleResponse(body, info) as List<Document>
    }

    private fun handleResponse(body: MutableMap<String,String>, info : Map<String,String>): List<Any> {
        val result = mutableListOf<Document>()
        var response: Response? = null
        var pageCount : Int = 0
        val thread = Thread {
            response = request(body, info)
        }
        thread.start()
        thread.join()

        when(response){
            is SearchResponse -> {
                (response as SearchResponse).documents.let {
                    result.addAll(it ?: emptyList())
                }
                pageCount = (response as SearchResponse).meta?.pageableCount ?: 0
            }
            else -> {}
        }

        if (pageCount >= MAX_PAGE) {
            val threads = mutableListOf<Thread>()
            for (i in START_PAGE + 1..MAX_PAGE) {
                body["page"] = i.toString()
                val t = Thread {
                    request(body, info)?.apply {
                        when(this){
                            is SearchResponse -> (this as SearchResponse).documents?.let { result.addAll(it) }
                            else -> {}
                        }
                    }
                }
                threads.add(t)
                t.start()
            }

            threads.forEach { it.join() }
        }


        return result
    }



    private fun request(body : Map<String,String>,info : Map<String,String>): Response? {
        val url = URL(BASE + URL)
        val conn = url.openConnection() as HttpsURLConnection

        conn.setReadTimeout(READ_TIMEOUT)
        conn.setConnectTimeout(CONNECT_TIMEOUT)
        conn.setRequestMethod(info["method"])
        conn.setDoOutput(true)
        conn.setRequestProperty("authorization", KEY)


        val body = body.entries.joinToString("&") { "${it.key}=${it.value}" }

        val os = conn.outputStream
        val writer = BufferedWriter(
            OutputStreamWriter(os, "UTF-8")
        )
        writer.write(body)
        writer.flush()
        writer.close()
        os.close()

        val responseCode = conn.getResponseCode()

        return when (responseCode) {
            HttpsURLConnection.HTTP_OK -> {
                val br = BufferedReader(InputStreamReader(conn.inputStream))
                onSuccess(br.readLine(),info["type"])
            }
            else -> null
        }
    }

    private fun onSuccess(json : String, type: String?) : Response?{
        val jsonObject = JSONObject(json)
        return when(type){
            "Search" -> {
                val meta = parseMeta(jsonObject.getJSONObject("meta"))
                val document = parseDocument(jsonObject.getJSONArray("documents"))

                SearchResponse(document,meta)
            }
            else -> null
        }
    }

    private fun parseDocument(documentJsonArray: JSONArray) : List<Document>{
        val result = mutableListOf<Document>()
        for (i in 0..<documentJsonArray.length()) {
            val documentJson = documentJsonArray.getJSONObject(i)
            result.add(
                Document(
                    documentJson.get("address_name").toString(),
                    documentJson.get("category_group_code").toString(),
                    documentJson.get("category_group_name").toString(),
                    documentJson.get("category_name").toString(),
                    documentJson.get("distance").toString(),
                    documentJson.get("id").toString(),
                    documentJson.get("phone").toString(),
                    documentJson.get("place_name").toString(),
                    documentJson.get("place_url").toString(),
                    documentJson.get("road_address_name").toString(),
                    documentJson.get("x").toString(),
                    documentJson.get("y").toString()
                )
            )
        }
        return result
    }
    private fun parseMeta(metaJsonObject : JSONObject) : Meta{
        val sameName = parseSameName(
            metaJsonObject.getJSONObject("same_name")
        )

        return Meta(
            metaJsonObject.get("is_end") as Boolean,
            metaJsonObject.get("pageable_count") as Int,
            sameName,
            metaJsonObject.get("total_count") as Int
        )
    }
    private fun parseSameName(sameNameJsonObject : JSONObject) : SameName {
        return SameName(
            sameNameJsonObject.get("keyword").toString(),
            listOf(sameNameJsonObject.getJSONArray("region").toString()),
            sameNameJsonObject.get("selected_region").toString()
        )
    }

    companion object {
        private const val BASE = BuildConfig.BASE_URL
        private const val KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        private const val URL = "v2/local/search/keyword.json"
        private const val READ_TIMEOUT = 1000
        private const val CONNECT_TIMEOUT = 1500
        private const val MAX_PAGE = 2
        private const val START_PAGE = 1
    }
}