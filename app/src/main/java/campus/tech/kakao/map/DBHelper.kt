package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE ${PlaceEntry.TABLE_NAME} (" +
                "${PlaceEntry.COLUMN_NAME} TEXT, " +
                "${PlaceEntry.COLUMN_ADDRESS} TEXT, " +
                "${PlaceEntry.COLUMN_CATEGORY} TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${PlaceEntry.TABLE_NAME}")
        onCreate(db)
    }
}