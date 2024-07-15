package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "place.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("DbHelper", "Database onCreate called")
        createTable(db)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        Log.d("DbHelper", "Database onOpen called")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("DbHelper", "Database onUpgrade called")
        db?.execSQL("DROP TABLE IF EXISTS ${PlaceContract.TABLE_NAME}")
        createTable(db)
    }

    private fun createTable(db: SQLiteDatabase?) {
        Log.d("DbHelper", "Creating table")
        db?.execSQL(
            "CREATE TABLE ${PlaceContract.TABLE_NAME} (" +
                    "${PlaceContract.COLUMN_NAME} VARCHAR(30) NOT NULL," +
                    "${PlaceContract.COLUMN_ADDRESS} VARCHAR(30) NOT NULL," +
                    "${PlaceContract.COLUMN_CATEGORY} VARCHAR(30) NOT NULL" +
                    ");"
        )
    }

    fun insertData(name: String, address: String, category: String) {
        writableDatabase.use { db ->
            Log.d("DbHelper", "Inserting data: $name, $address, $category")
            if(!isDataExists(name, address, category, db)) {
                val values = ContentValues().apply {
                    put(PlaceContract.COLUMN_NAME, name)
                    put(PlaceContract.COLUMN_ADDRESS, address)
                    put(PlaceContract.COLUMN_CATEGORY, category)
                }
                db.insert(PlaceContract.TABLE_NAME, null, values)
                Log.d("DbHelper", "Inserted data: $name, $address, $category")
            }
        }
    }

    //DB가 비어있는지 확인
    fun isDBEmpty(): Boolean {
        return readableDatabase.use { db ->
            db.rawQuery("SELECT COUNT(*) FROM ${PlaceContract.TABLE_NAME}", null).use { cursor ->
                if(cursor.moveToFirst()) {
                    cursor.getInt(0) == 0
                } else {
                    true
                }
            }
        }
    }

    //데이터 추가 시 중복 방지
    private fun isDataExists(name: String, address: String, category: String, db: SQLiteDatabase): Boolean {
        return db.rawQuery(
            "SELECT 1 FROM ${PlaceContract.TABLE_NAME} WHERE ${PlaceContract.COLUMN_NAME} = ? AND ${PlaceContract.COLUMN_ADDRESS} = ? AND ${PlaceContract.COLUMN_CATEGORY} = ?",
            arrayOf(name, address, category)
        ).use { cursor ->
            cursor.moveToFirst()
        }
    }

    fun searchDatabase(query: String): List<SearchResult> {
        val results = mutableListOf<SearchResult>()

        readableDatabase.use { db ->
            db.rawQuery(
                "SELECT * FROM ${PlaceContract.TABLE_NAME} WHERE " +
                        "${PlaceContract.COLUMN_NAME} LIKE ? OR " +
                        "${PlaceContract.COLUMN_ADDRESS} LIKE ? OR " +
                        "${PlaceContract.COLUMN_CATEGORY} LIKE ?",
                arrayOf("%$query%", "%$query%", "%$query%")
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.COLUMN_NAME))
                    val address =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.COLUMN_ADDRESS))
                    val category =
                        cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.COLUMN_CATEGORY))
                    results.add(SearchResult(name, address, category))
                    Log.d("DbHelper", "Found data: $name, $address, $category")
                }
            }
        }
        Log.d("DbHelper", "Search results: $results")
        return results
    }
}