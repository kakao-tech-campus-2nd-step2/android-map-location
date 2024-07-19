package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${PlaceContract.Place.TABLE_NAME} (" +
            "  ${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "  ${PlaceContract.Place.COLUMN_NAME} TEXT not null UNIQUE," +
            "  ${PlaceContract.Place.COLUMN_ADDRESS} TEXT," +
            "  ${PlaceContract.Place.COLUMN_CATEGORY} TEXT," +
            "  ${PlaceContract.Place.COLUMN_X} TEXT," +
            "  ${PlaceContract.Place.COLUMN_Y} TEXT)"

private const val SQL_CREATE_TRIGGER =
    "CREATE TRIGGER IF NOT EXISTS no_duplicate_places " +
            "BEFORE INSERT ON ${PlaceContract.Place.TABLE_NAME} " +
            "FOR EACH ROW " +
            "WHEN EXISTS (SELECT 1 FROM ${PlaceContract.Place.TABLE_NAME} WHERE ${PlaceContract.Place.COLUMN_NAME} = NEW.${PlaceContract.Place.COLUMN_NAME}) " +
            "BEGIN " +
            "  SELECT RAISE(IGNORE); " +
            "END;"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PlaceContract.Place.TABLE_NAME}"

class PlaceDatabaseHelper(context: Context, databaseName: String) : SQLiteOpenHelper(context, databaseName, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        db.execSQL(SQL_CREATE_TRIGGER)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        const val DATABASE_VERSION = 1
    }
}