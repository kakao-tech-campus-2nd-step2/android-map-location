package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.dto.Place

class SearchHistoryDBHelper(context: Context) : SQLiteOpenHelper(context, "SearchHistory.db", null, 2) { // 버전을 2로 변경

    override fun onCreate(db: SQLiteDatabase?) {
        createSearchHistoryTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("DROP TABLE IF EXISTS SearchHistoryTable")
            createSearchHistoryTable(db)
        }
    }

    private fun createSearchHistoryTable(db: SQLiteDatabase?) {
        val searchHistoryTableSQL = """
            CREATE TABLE SearchHistoryTable (
                idx INTEGER PRIMARY KEY AUTOINCREMENT,
                address_name TEXT NOT NULL,
                category_group_code TEXT,
                category_group_name TEXT,
                category_name TEXT NOT NULL,
                distance TEXT,
                id TEXT NOT NULL,
                phone TEXT,
                place_name TEXT NOT NULL,
                place_url TEXT,
                road_address_name TEXT,
                x TEXT NOT NULL,
                y TEXT NOT NULL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """
        db?.execSQL(searchHistoryTableSQL)
    }

    fun insertSearchHistory(place: Place): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("address_name", place.address_name)
            put("category_group_code", place.category_group_code)
            put("category_group_name", place.category_group_name)
            put("category_name", place.category_name)
            put("distance", place.distance)
            put("id", place.id)
            put("phone", place.phone)
            put("place_name", place.place_name)
            put("place_url", place.place_url)
            put("road_address_name", place.road_address_name)
            put("x", place.x)
            put("y", place.y)
        }
        return db.insert("SearchHistoryTable", null, values)
    }

    fun deleteSearchHistoryByName(name: String): Int {
        val db = writableDatabase
        return db.delete("SearchHistoryTable", "place_name = ?", arrayOf(name))
    }

    fun getAllSearchHistory(): MutableList<Place> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM SearchHistoryTable ORDER BY timestamp DESC", null)
        val searchHistory = mutableListOf<Place>()
        if (cursor.moveToFirst()) {
            do {
                val address_name = cursor.getString(cursor.getColumnIndexOrThrow("address_name"))
                val category_group_code = cursor.getString(cursor.getColumnIndexOrThrow("category_group_code"))
                val category_group_name = cursor.getString(cursor.getColumnIndexOrThrow("category_group_name"))
                val category_name = cursor.getString(cursor.getColumnIndexOrThrow("category_name"))
                val distance = cursor.getString(cursor.getColumnIndexOrThrow("distance"))
                val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                val place_name = cursor.getString(cursor.getColumnIndexOrThrow("place_name"))
                val place_url = cursor.getString(cursor.getColumnIndexOrThrow("place_url"))
                val road_address_name = cursor.getString(cursor.getColumnIndexOrThrow("road_address_name"))
                val x = cursor.getString(cursor.getColumnIndexOrThrow("x"))
                val y = cursor.getString(cursor.getColumnIndexOrThrow("y"))

                searchHistory.add(Place(address_name, category_group_code, category_group_name, category_name, distance, id, phone, place_name, place_url, road_address_name, x, y))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return searchHistory
    }
}
