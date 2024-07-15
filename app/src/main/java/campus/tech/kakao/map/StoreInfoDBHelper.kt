package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_SEARCH_KEYWORD_CREATE_ENTRIES =
    "CREATE TABLE ${SearchKeywordEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${SearchKeywordEntry.SEARCH_KEYWORD} TEXT UNIQUE)"

private const val SQL_SEARCH_WORD_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${SearchKeywordEntry.TABLE_NAME}"

class StoreInfoDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "SearchKeyword.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_SEARCH_KEYWORD_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_SEARCH_WORD_DELETE_ENTRIES)
        onCreate(db)
    }
}