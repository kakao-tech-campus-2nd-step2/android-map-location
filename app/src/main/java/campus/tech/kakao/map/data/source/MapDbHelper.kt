package campus.tech.kakao.map.data.source

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.MapContract

class MapDbHelper(mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
        db?.execSQL(SQL_CREATE_ENTRIES_HISTORY)
        db?.execSQL(SQL_CREATE_ENTRIES_LAST_LOCATION)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        db?.execSQL(SQL_DELETE_ENTRIES_HISTORY)
        db?.execSQL(SQL_DELETE_ENTRIES_LAST_LOCATION)
        onCreate(db)
    }

//    private fun initializeDb(db: SQLiteDatabase?) {
//        for (idx in 1..10) {
//            val exampleCafeValue = ContentValues()
//            exampleCafeValue.put(MapContract.MapEntry.COLUMN_NAME_NAME, "카페$idx")
//            exampleCafeValue.put(MapContract.MapEntry.COLUMN_NAME_CATEGORY, Location.CAFE)
//            exampleCafeValue.put(MapContract.MapEntry.COLUMN_NAME_ADDRESS, "서울 성동구 성수동 $idx")
//            db?.insert(MapContract.MapEntry.TABLE_NAME, null, exampleCafeValue)
//
//            val examplePharValue = ContentValues()
//            examplePharValue.put(MapContract.MapEntry.COLUMN_NAME_NAME, "약국$idx")
//            examplePharValue.put(MapContract.MapEntry.COLUMN_NAME_CATEGORY, Location.PHARMACY)
//            examplePharValue.put(MapContract.MapEntry.COLUMN_NAME_ADDRESS, "서울 성동구 성수동 $idx")
//            db?.insert(MapContract.MapEntry.TABLE_NAME, null, examplePharValue)
//        }
//    }

    fun clearResult(db: SQLiteDatabase?) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    fun clearLastLocation(db: SQLiteDatabase?) {
        db?.execSQL(SQL_DELETE_ENTRIES_LAST_LOCATION)
        db?.execSQL(SQL_CREATE_ENTRIES_LAST_LOCATION)
    }

    companion object {
        const val DATABASE_NAME = "map.db"
        const val DATABASE_VERSION = 1

        private const val SQL_CREATE_ENTRIES = """
            CREATE TABLE ${MapContract.MapEntry.TABLE_NAME} (
            ${MapContract.MapEntry.COLUMN_NAME_ID} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_NAME} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_CATEGORY} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_ADDRESS} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_X} TEXT, 
            ${MapContract.MapEntry.COLUMN_NAME_Y} TEXT);
            """

        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS ${MapContract.MapEntry.TABLE_NAME}"

        private const val SQL_CREATE_ENTRIES_HISTORY = """
            CREATE TABLE ${MapContract.MapEntry.TABLE_NAME_HISTORY} (
            ${MapContract.MapEntry.COLUMN_NAME_ID} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_NAME} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_CATEGORY} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_ADDRESS} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_X} TEXT, 
            ${MapContract.MapEntry.COLUMN_NAME_Y} TEXT);
            """

        private const val SQL_DELETE_ENTRIES_HISTORY =
            "DROP TABLE IF EXISTS ${MapContract.MapEntry.TABLE_NAME_HISTORY}"

        private const val SQL_CREATE_ENTRIES_LAST_LOCATION = """
            CREATE TABLE ${MapContract.MapEntry.TABLE_NAME_LAST_LOCATION} (
            ${MapContract.MapEntry.COLUMN_NAME_ID} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_NAME} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_CATEGORY} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_ADDRESS} TEXT,
            ${MapContract.MapEntry.COLUMN_NAME_X} TEXT, 
            ${MapContract.MapEntry.COLUMN_NAME_Y} TEXT);
            """

        private const val SQL_DELETE_ENTRIES_LAST_LOCATION =
            "DROP TABLE IF EXISTS ${MapContract.MapEntry.TABLE_NAME_LAST_LOCATION}"
    }
}