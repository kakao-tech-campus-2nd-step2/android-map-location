package campus.tech.kakao.map.repository.keyword

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class KeywordDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: android.database.sqlite.SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(
        db: android.database.sqlite.SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int,
    ) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${KeywordContract.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${KeywordContract.RECENT_KEYWORD} TEXT)"

        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS ${KeywordContract.TABLE_NAME}"

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "keyword.db"
    }
}
