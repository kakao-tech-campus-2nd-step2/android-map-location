package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SearchRepo(private val databaseHelper: DatabaseHelper) {
    fun insertSearchData(data: String) {
        databaseHelper.insertData(data)
    }

    fun getAllSearchData(): List<String> {
        return databaseHelper.getAllData()
    }
}


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "database_name.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE search_data (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "data TEXT" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData(data: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("data", data)
        }
        db.insert("search_data", null, values)
        db.close()
    }

    fun getAllData(): List<String> {
        val db = readableDatabase
        val cursor = db.query("search_data", arrayOf("data"), null, null, null, null, null)
        val dataList = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                dataList.add(getString(getColumnIndexOrThrow("data")))
            }
        }
        cursor.close()
        db.close()
        return dataList
    }
}

