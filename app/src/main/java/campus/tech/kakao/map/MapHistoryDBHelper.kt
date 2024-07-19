package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MapHistoryDBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE ${MapHistoryEntry.TABLE_NAME} (" +
                "${MapHistoryEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${MapHistoryEntry.COLUMN_X} REAL, " +
                "${MapHistoryEntry.COLUMN_Y} REAL)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${MapHistoryEntry.TABLE_NAME}")
        onCreate(db)
    }
}
