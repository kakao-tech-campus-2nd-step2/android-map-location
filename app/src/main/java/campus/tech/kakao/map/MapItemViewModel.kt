package campus.tech.kakao.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapItemViewModel(context: Context) : ViewModel() {
    private val mapItemDB = KakaoMapItemDbHelper(context)
    val networkService = NetworkService()

    private val _kakaoMapItemList: MutableLiveData<List<KakaoMapItem>> = MutableLiveData()
    val kakaoMapItemList : LiveData<List<KakaoMapItem>> get() = _kakaoMapItemList

    private val _selectItemList: MutableLiveData<List<SelectMapItem>> = MutableLiveData()
    val selectItemList : LiveData<List<SelectMapItem>> get() = _selectItemList

    init {
        if(!mapItemDB.selectItemDao.checkTableExist()) {
            createMapItemDB()
        }
        _selectItemList.postValue(mapItemDB.selectItemDao.makeAllSelectItemList())
    }

    private fun createMapItemDB() {
        mapItemDB.onCreate(mapItemDB.writableDatabase)
    }

    private fun updateMapItemDB() {
        mapItemDB.onUpgrade(mapItemDB.writableDatabase, 1, 2)
    }

    fun makeAllSelectItemList() {
        _selectItemList.postValue(mapItemDB.selectItemDao.makeAllSelectItemList())
    }

    suspend fun searchKakaoMapItem(category: String) {
        _kakaoMapItemList.postValue(networkService.searchKakaoMapItem(category))
    }

    fun insertSelectItem(name: String, id: String) {
        val isExist = mapItemDB.selectItemDao.checkItemInDB(id)
        if(isExist) {
            mapItemDB.selectItemDao.deleteSelectItem(id)
        }
        mapItemDB.selectItemDao.insertSelectItem(name, id)
        _selectItemList.postValue(mapItemDB.selectItemDao.makeAllSelectItemList())
    }

    fun deleteSelectItem(id: String) {
        mapItemDB.selectItemDao.deleteSelectItem(id)
        _selectItemList.postValue(mapItemDB.selectItemDao.makeAllSelectItemList())
    }
}