package campus.tech.kakao.map.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import campus.tech.kakao.map.model.SavedSearchWord

class SavedSearchWordDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_PLACE_ID TEXT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_LONGITUDE TEXT,
                $COLUMN_LATITUDE TEXT
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int,
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertOrUpdateSearchWord(searchWord: SavedSearchWord) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.delete(TABLE_NAME, "$COLUMN_PLACE_ID = ?", arrayOf(searchWord.placeId))

            val contentValues =
                ContentValues().apply {
                    put(COLUMN_NAME, searchWord.name)
                    put(COLUMN_PLACE_ID, searchWord.placeId)
                    put(COLUMN_ADDRESS, searchWord.address)
                    put(COLUMN_LONGITUDE, searchWord.longitude)
                    put(COLUMN_LATITUDE, searchWord.latitude)
                }
            db.insert(TABLE_NAME, null, contentValues)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("dbError", "Error while delete and insert: ${e.message}")
        } finally {
            db.endTransaction()
        }
    }

    fun getAllSearchWords(): List<SavedSearchWord> {
        val searchWords = mutableListOf<SavedSearchWord>()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(query, null)
        try {
            cursor?.let {
                while (it.moveToNext()) {
                    val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                    val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                    val placeId = it.getString(it.getColumnIndexOrThrow(COLUMN_PLACE_ID))
                    val address = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDRESS))
                    val longitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                    val latitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LATITUDE))
                    val savedSearchWord =
                        SavedSearchWord(id, name, placeId, address, longitude, latitude)
                    searchWords.add(savedSearchWord)
                }
            }
        } catch (e: Exception) {
            Log.e("dbError", "Error while fetching all search words: ${e.message}")
        } finally {
            cursor?.close()
        }
        return searchWords
    }

    fun deleteSearchWordById(id: Long) {
        writableDatabase.use { db ->
            db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        }
    }

    companion object {
        const val DATABASE_NAME = "search_words.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "search_words_data"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PLACE_ID = "place_id"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_LONGITUDE = "longitude"
        const val COLUMN_LATITUDE = "latitude"
    }
}
