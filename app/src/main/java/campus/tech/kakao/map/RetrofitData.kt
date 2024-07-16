package campus.tech.kakao.map

import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.PlaceResponse
import campus.tech.kakao.map.dto.UrlContract
import campus.tech.kakao.map.dto.UrlContract.AUTHORIZATION
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitData private constructor() {
	private val _documents = MutableLiveData<List<Document>>()
	val retrofitService = Retrofit.Builder()
		.baseUrl(UrlContract.BASE_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(RetrofitService::class.java)

	fun searchPlace(query : String){
		retrofitService.requestPlaces(AUTHORIZATION,query).enqueue(object : Callback<PlaceResponse> {
			override fun onResponse(
				call: Call<PlaceResponse>,
				response: Response<PlaceResponse>
			) {
				if (response.isSuccessful) {
					val documentList = mutableListOf<Document>()
					val body = response.body()
					body?.documents?.forEach {document ->
						documentList.add(Document(
							document.placeName,
							document.categoryGroupName,
							document.addressName,
							document.longitude,
							document.latitude))
					}
					_documents.value = documentList
				}
			}

			override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
			}
		})
	}
	fun getDocuments() = _documents

	companion object {
		private var instance: RetrofitData? = null

		fun getInstance(): RetrofitData {
			if (instance == null) {
				instance = RetrofitData()
			}
			return instance!!
		}
	}
}