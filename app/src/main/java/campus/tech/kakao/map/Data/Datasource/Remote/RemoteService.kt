package campus.tech.kakao.map.Data.Datasource.Remote

import campus.tech.kakao.map.Data.Datasource.Remote.Response.Document

interface RemoteService {
    fun getPlaceResponse(query: String): List<Document>
}