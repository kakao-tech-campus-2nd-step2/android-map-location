package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

class PlaceDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MyPlace.db"

        private const val SQL_CREATE_RESEARCH_TABLE =
            "CREATE TABLE ${MyPlaceContract.Research.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${MyPlaceContract.Research.COLUMN_IMG} INTEGER," +
                "${MyPlaceContract.Research.COLUMN_NAME} TEXT," +
                "${MyPlaceContract.Research.COLUMN_CATEGORY} TEXT," +
                "${MyPlaceContract.Research.COLUMN_LOCATION} TEXT)"

        private const val SQL_DELETE_RESEARCH= "DROP TABLE IF EXISTS ${MyPlaceContract.Research.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("PlaceDbHelper", "Creating tables")
        db?.execSQL(SQL_CREATE_RESEARCH_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_RESEARCH)
    }
}