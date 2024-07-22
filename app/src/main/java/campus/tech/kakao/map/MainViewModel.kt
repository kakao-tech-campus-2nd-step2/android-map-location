package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import campus.tech.kakao.map.dto.MapPosition.getMapPosition
import campus.tech.kakao.map.url.RetrofitData.Companion.getInstance
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.SearchWord
import campus.tech.kakao.map.dto.SearchWordDbHelper

class MainViewModel(application: Application): AndroidViewModel(application) {

	private val wordDbHelper = SearchWordDbHelper(application)
	val wordList: LiveData<List<SearchWord>> get() =  wordDbHelper.getSearchWords()

	private val retrofitData = getInstance()
	val documentList: LiveData<List<Document>> get() = retrofitData.getDocuments()




	fun addWord(document: Document){
		wordDbHelper.addWord(wordfromDocument(document))
	}

	private fun wordfromDocument(document: Document): SearchWord {
		return SearchWord(document.placeName, document.categoryGroupName, document.addressName)
	}
	fun deleteWord(word: SearchWord){
		wordDbHelper.deleteWord(word)
	}

	fun loadWord(){
		wordDbHelper.updateSearchWords()
	}

	fun searchLocalAPI(query: String){
		retrofitData.searchPlace(query)
	}

	override fun onCleared() {
		super.onCleared()
		wordDbHelper.close()
	}

	fun setMapInfo(document: Document){
		getMapPosition(getApplication()).setMapInfo(document)
	}

	fun getMapInfo():List<String>{
		val latitude = getMapPosition(getApplication()).getPreferences(LATITUDE,INIT_LATITUDE)
		val longitude = getMapPosition(getApplication()).getPreferences(LONGITUDE,INIT_LONGITUDE)
		val placeName = getMapPosition(getApplication()).getPreferences(PLACE_NAME,"")
		val addressName = getMapPosition(getApplication()).getPreferences(ADDRESS_NAME,"")
		return listOf(latitude, longitude, placeName, addressName)
	}

	companion object{
		const val INIT_LATITUDE = "37.402005"
		const val INIT_LONGITUDE = "127.108621"
		const val LATITUDE = "latitude"
		const val LONGITUDE = "longitude"
		const val PLACE_NAME = "placeName"
		const val ADDRESS_NAME = "addressName"
	}
}