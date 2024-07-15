package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class MapDbHelper(mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
        db?.execSQL(SQL_CREATE_ENTRIES_HISTORY)
//        initializeDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        db?.execSQL(SQL_DELETE_ENTRIES_HISTORY)
        onCreate(db)
    }

    private fun initializeDb(db: SQLiteDatabase?) {
        for (idx in 1..10) {
            val exampleCafeValue = ContentValues()
            exampleCafeValue.put(MapContract.MapEntry.COLUMN_NAME_NAME, "카페$idx")
            exampleCafeValue.put(MapContract.MapEntry.COLUMN_NAME_CATEGORY, Location.CAFE)
            exampleCafeValue.put(MapContract.MapEntry.COLUMN_NAME_ADDRESS, "서울 성동구 성수동 $idx")
            db?.insert(MapContract.MapEntry.TABLE_NAME, null, exampleCafeValue)

            val examplePharValue = ContentValues()
            examplePharValue.put(MapContract.MapEntry.COLUMN_NAME_NAME, "약국$idx")
            examplePharValue.put(MapContract.MapEntry.COLUMN_NAME_CATEGORY, Location.PHARMACY)
            examplePharValue.put(MapContract.MapEntry.COLUMN_NAME_ADDRESS, "서울 성동구 성수동 $idx")
            db?.insert(MapContract.MapEntry.TABLE_NAME, null, examplePharValue)
        }
    }

    fun clearDb(db: SQLiteDatabase?) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    companion object {
        const val DATABASE_NAME = "map.db"
        const val DATABASE_VERSION = 1

        private const val SQL_CREATE_ENTRIES = """
            CREATE TABLE ${MapContract.MapEntry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${MapContract.MapEntry.COLUMN_NAME_NAME} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_CATEGORY} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_ADDRESS} TEXT); 
            """

        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS ${MapContract.MapEntry.TABLE_NAME}"

        private const val SQL_CREATE_ENTRIES_HISTORY = """
            CREATE TABLE ${MapContract.MapEntry.TABLE_NAME_HISTORY} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${MapContract.MapEntry.COLUMN_NAME_NAME} TEXT);
            """

        private const val SQL_DELETE_ENTRIES_HISTORY =
            "DROP TABLE IF EXISTS ${MapContract.MapEntry.TABLE_NAME_HISTORY}"
    }
}