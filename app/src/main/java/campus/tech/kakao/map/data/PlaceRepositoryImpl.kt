package campus.tech.kakao.map.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.data.net.KakaoApiClient
import campus.tech.kakao.map.util.PlaceContract
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceRepositoryImpl(context: Context):
    SQLiteOpenHelper(context, PlaceContract.DATABASE_NAME, null, 1),
    PlaceRepository {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(PlaceContract.CREATE_QUERY)
        db?.execSQL(PlaceContract.CREATE_LOG_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(PlaceContract.DROP_QUERY)
        db?.execSQL(PlaceContract.DROP_LOG_QUERY)
        onCreate(db)
    }
    override suspend fun getPlaces(keyword: String): List<Place> =
        withContext(Dispatchers.IO){
            val resultPlaces = mutableListOf<Place>()
            for (page in 1..3) {
                val response = KakaoApiClient.api.getSearchKeyword(
                    key = BuildConfig.KAKAO_REST_API_KEY,
                    query = keyword,
                    size = 15,
                    page = page
                )
                if (response.isSuccessful) {
                    response.body()?.documents?.let { resultPlaces.addAll(it) }
                } else throw RuntimeException("통신 에러 발생")
            }
            updatePlaces(resultPlaces)
            resultPlaces
        }


    override suspend fun updatePlaces(places: List<Place>) {
        val db = writableDatabase

        db.execSQL(PlaceContract.DELETE_QUERY)
        places.forEach {
            val values = ContentValues().apply {
                put(PlaceContract.COLUMN_ID, it.id)
                put(PlaceContract.COLUMN_NAME, it.place)
                put(PlaceContract.COLUMN_LOCATION, it.address)
                put(PlaceContract.COLUMN_TYPE, it.category)
                put(PlaceContract.COLUMN_X_POS, it.xPos)
                put(PlaceContract.COLUMN_Y_POS, it.yPos)
            }
            db.insert(PlaceContract.TABLE_NAME, null, values)
        }
    }

    override fun updateLogs(logs: List<Place>) {
        val db = writableDatabase
        db.execSQL(PlaceContract.DELETE_LOG_QUERY)
        logs.forEach { placeLog ->
            val values = ContentValues().apply {
                put(PlaceContract.COLUMN_LOG_ID, placeLog.id)
                put(PlaceContract.COLUMN_LOG_NAME, placeLog.place)
            }
            db.insert(PlaceContract.TABLE_LOG_NAME, null, values)
        }
    }

    override fun removeLog(id: String) {
        val db = writableDatabase
        db.delete(PlaceContract.TABLE_LOG_NAME, "${PlaceContract.COLUMN_LOG_ID}=?", arrayOf(id))
    }

    override fun getLogs(): List<Place> {
        val logs = mutableListOf<Place>()
        val cursor = readableDatabase.query(
            PlaceContract.TABLE_LOG_NAME,
            null, null, null, null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_LOG_NAME))
                val id = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_LOG_ID))
                logs.add(Place(id,name, "", "", "",""))
            }
        }
        return logs
    }

    companion object {

        @Volatile
        private var INSTANCE: PlaceRepositoryImpl? = null

        fun getInstance(context: Context): PlaceRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PlaceRepositoryImpl(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
