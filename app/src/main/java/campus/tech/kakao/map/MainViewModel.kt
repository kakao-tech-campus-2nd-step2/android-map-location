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

	private val initLatitude = "37.402005"
	private val initLongitude = "127.108621"


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
		val latitude = getMapPosition(getApplication()).getPreferences("latitude",initLatitude)
		val longitude = getMapPosition(getApplication()).getPreferences("longitude",initLongitude)
		val placeName = getMapPosition(getApplication()).getPreferences("placeName","")
		val addressName = getMapPosition(getApplication()).getPreferences("addressName","")
		return listOf(latitude, longitude, placeName, addressName)
	}
}