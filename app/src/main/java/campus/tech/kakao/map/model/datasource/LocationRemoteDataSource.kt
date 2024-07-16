package campus.tech.kakao.map.model.datasource

import android.util.Log
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.Location.Companion.toLocation
import campus.tech.kakao.map.model.LocationDto
import campus.tech.kakao.map.model.repository.KakaoAPI
import campus.tech.kakao.map.model.repository.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRemoteDataSource {
    companion object{
        private const val RESULT_SIZE = 15
    }

    private val client = RetrofitInstance.getInstance().create(KakaoAPI::class.java)

    suspend fun getLocations(keyword: String): List<Location> {

        return withContext(Dispatchers.IO){
            val response = client.searchFromKeyword(keyword, RESULT_SIZE)
            val locationDtos: List<LocationDto> = response.body()?.documents ?: emptyList()
            Log.d("jieun", "locationDtos: " + locationDtos)
            toLocations(locationDtos)
        }
    }

    private fun toLocations(locationDtos: List<LocationDto>) =
        locationDtos.map {
            it.toLocation()
        }
}