package campus.tech.kakao.map.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.util.PlaceContract
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

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

    override fun getPlaces(placeName: String): List<Place> {
        val places = mutableListOf<Place>()
        val cursor = readableDatabase.query(
            PlaceContract.TABLE_NAME,
            null, "${PlaceContract.COLUMN_NAME} LIKE ?", arrayOf("${placeName}%"), null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_NAME))
                val place = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_LOCATION))
                val type = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_TYPE))
                places.add(Place(id, name, place, type))
            }
        }
        return places
    }

    override fun getAllPlaces(): List<Place> {
        val places = mutableListOf<Place>()
        val cursor = readableDatabase.query(
            PlaceContract.TABLE_NAME,
            null, null, null, null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_NAME))
                val place = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_LOCATION))
                val type = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_TYPE))
                places.add(Place(id, name, place, type))
            }
        }
        return places
    }

    override fun updatePlaces(places: List<Place>) {
        val db = writableDatabase

        db.execSQL(PlaceContract.DELETE_QUERY)
        places.forEach {
            val values = ContentValues().apply {
                put(PlaceContract.COLUMN_ID, it.id)
                put(PlaceContract.COLUMN_NAME, it.place)
                put(PlaceContract.COLUMN_LOCATION, it.address)
                put(PlaceContract.COLUMN_TYPE, it.category)
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
                logs.add(Place(id,name, "", ""))
            }
        }
        return logs
    }

    companion object {
        private const val NO_LOGS_FOUND = 0

        @Volatile
        private var INSTANCE: PlaceRepositoryImpl? = null

        fun getInstance(context: Context): PlaceRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PlaceRepositoryImpl(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
