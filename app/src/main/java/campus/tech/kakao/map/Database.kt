package campus.tech.kakao.map

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Database(context: Context) : SQLiteOpenHelper(context, "place.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${MapContract.TABLE_CAFE} (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${MapContract.COLUMN_NAME} TEXT," +
                    "${MapContract.COLUMN_ADDRESS} TEXT," +
                    "${MapContract.COLUMN_CATEGORY} TEXT" +
                    ")"
        )
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${MapContract.TABLE_PHARMACY} (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${MapContract.COLUMN_NAME} TEXT," +
                    "${MapContract.COLUMN_ADDRESS} TEXT," +
                    "${MapContract.COLUMN_CATEGORY} TEXT" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${MapContract.TABLE_CAFE}")
        db?.execSQL("DROP TABLE IF EXISTS ${MapContract.TABLE_PHARMACY}")
        onCreate(db)
    }


    fun searchPlaces(searchText: String): List<Map<String, String>> {
        val db = readableDatabase
        val cafes = queryPlaces(db, MapContract.TABLE_CAFE, searchText)
        val pharmacies = queryPlaces(db, MapContract.TABLE_PHARMACY, searchText)
        db.close()

        val dataList = mutableListOf<Map<String, String>>()
        dataList.addAll(cafes)
        dataList.addAll(pharmacies)
        return dataList
    }

    private fun queryPlaces(
        db: SQLiteDatabase,
        tableName: String,
        searchText: String
    ): List<Map<String, String>> {
        val cursor: Cursor = db.query(
            tableName,
            null,
            "${MapContract.COLUMN_NAME} LIKE ? OR ${MapContract.COLUMN_ADDRESS} LIKE ?",
            arrayOf("%$searchText%", "%$searchText%"),
            null,
            null,
            null
        )

        val results = mutableListOf<Map<String, String>>()
        while (cursor.moveToNext()) {
            val row = mutableMapOf<String, String>()
            row[MapContract.COLUMN_NAME] =
                cursor.getString(cursor.getColumnIndexOrThrow(MapContract.COLUMN_NAME))
            row[MapContract.COLUMN_ADDRESS] =
                cursor.getString(cursor.getColumnIndexOrThrow(MapContract.COLUMN_ADDRESS))
            row[MapContract.COLUMN_CATEGORY] =
                cursor.getString(cursor.getColumnIndexOrThrow(MapContract.COLUMN_CATEGORY))
            results.add(row)
        }
        cursor.close()
        return results
    }
}

