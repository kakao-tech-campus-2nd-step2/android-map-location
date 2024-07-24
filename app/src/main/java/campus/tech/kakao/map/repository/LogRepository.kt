package campus.tech.kakao.map.repository

import campus.tech.kakao.map.base.MyApplication
import campus.tech.kakao.map.data.db.AppDatabase
import campus.tech.kakao.map.data.db.PlaceDao
import campus.tech.kakao.map.data.db.entity.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class LogRepository(private val application: MyApplication): LogRepositoryInterface {
    private val placeDao: PlaceDao = AppDatabase.getDatabase(application).placeDao()
    private var logList = mutableListOf<Place>()

    override fun getAllLogs(): List<Place> {
        runBlocking {
            withContext(Dispatchers.IO) {
                logList = placeDao.getAllLogs().toMutableList()
            }
        }
        return logList
    }

    override fun haveAnyLog() : Boolean {
        return runBlocking {
            withContext(Dispatchers.IO) {
                placeDao.getPlaceCount() > 0
            }
        }
    }

    override fun insertLog(place: Place): List<Place> {
        CoroutineScope(Dispatchers.IO).launch {
            placeDao.insertLog(place)
            withContext(Dispatchers.Main) {
                logList.add(place)
            }
        }
        return logList
    }

    override fun deleteLog(place: Place): List<Place> {
        CoroutineScope(Dispatchers.IO).launch {
            placeDao.deleteLog(place)
            withContext(Dispatchers.Main) {
                logList.remove(place)
            }
        }
        return logList
    }
}