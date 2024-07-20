package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import campus.tech.kakao.map.MyApplication.Companion.mapPosition
import campus.tech.kakao.map.RetrofitData.Companion.getInstance

class MainViewModel(application: Application): AndroidViewModel(application) {

	private val wordDbHelper = SearchWordDbHelper(application)
	val wordList: LiveData<List<SearchWord>> get() =  wordDbHelper.getSearchWords()

	private val retrofitData = getInstance()
	val documentList: LiveData<List<Document>> get() = retrofitData.getDocuments()

	private val initLatitude = "37.402005"
	private val initLongitude = "127.108621"

	fun getInitLatitude() = initLatitude
	fun getInitLongitude() = initLongitude

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
		mapPosition.setMapInfo(document)
	}

	fun getMapInfo():List<String>{
		val latitude = mapPosition.getPreferences("latitude",initLatitude)
		val longitude = mapPosition.getPreferences("longitude",initLongitude)
		val placeName = mapPosition.getPreferences("placeName","")
		val addressName = mapPosition.getPreferences("addressName","")
		return listOf(latitude, longitude, placeName, addressName)
	}
}