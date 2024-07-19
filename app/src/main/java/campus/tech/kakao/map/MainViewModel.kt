package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import campus.tech.kakao.map.dbHelper.SearchWordDbHelper
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.MapPositionContract
import campus.tech.kakao.map.dto.SearchWord
import campus.tech.kakao.map.MyApplication.Companion.mapPosition
import campus.tech.kakao.map.RetrofitData.Companion.getInstance

class MainViewModel(application: Application): AndroidViewModel(application) {

	private val wordDbHelper = SearchWordDbHelper(application)
	val wordList: LiveData<List<SearchWord>> get() =  wordDbHelper.getSearchWords()

	private val retrofitData = getInstance()
	val documentList: LiveData<List<Document>> get() = retrofitData.getDocuments()

	fun addWord(document: Document){
		wordDbHelper.addWord(wordfromDocument(document))
	}

	private fun wordfromDocument(document: Document): SearchWord {
		return SearchWord(document.placeName, document.addressName, document.categoryGroupName)
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

	fun getMapInfo(document: Document){
		mapPosition.setPreferences(MapPositionContract.PREFERENCE_KEY_LATITUDE, document.latitude)
		mapPosition.setPreferences(MapPositionContract.PREFERENCE_KEY_LONGITUDE, document.longitude)
		mapPosition.setPreferences(MapPositionContract.PREFERENCE_KEY_PLACENAME, document.placeName)
		mapPosition.setPreferences(MapPositionContract.PREFERENCE_KEY_ADDRESSNAME, document.addressName)
	}
}