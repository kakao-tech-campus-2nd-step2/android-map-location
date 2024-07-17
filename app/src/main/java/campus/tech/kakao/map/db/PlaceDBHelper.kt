package campus.tech.kakao.map.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class PlaceDBHelper(context: Context) : SQLiteOpenHelper(context, PlaceContract.DATABASE_NAME, null, PlaceContract.VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        createLocalTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${PlaceContract.PlaceEntry.TABLE_NAME};")
        db.execSQL("DROP TABLE IF EXISTS ${PlaceContract.SavedPlaceEntry.TABLE_NAME};")
        createLocalTables(db)
    }

    fun createLocalTables(db: SQLiteDatabase){
        createPlaceTable(db)
        createSavedPlaceTable(db)
    }

    fun createPlaceTable(db: SQLiteDatabase){
        db.execSQL(
            "CREATE TABLE " +
                    "${PlaceContract.PlaceEntry.TABLE_NAME}( " +
                    " ${PlaceContract.PlaceEntry.COLUMN_NAME} varchar(60) not null ," +
                    " ${PlaceContract.PlaceEntry.COLUMN_LOCATION} varchar(255) not null, " +
                    " ${PlaceContract.PlaceEntry.COLUMN_CATEGORY} varchar(30) );"
        )
    }

    fun createSavedPlaceTable(db: SQLiteDatabase){
        db.execSQL(
            "CREATE TABLE " +
                    "${PlaceContract.SavedPlaceEntry.TABLE_NAME}( " +
                    " ${PlaceContract.SavedPlaceEntry.COLUMN_NAME} varchar(60) not null);"
        )
    }

    fun insertPlaceData(name: String, location: String, category: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(PlaceContract.PlaceEntry.COLUMN_NAME, name)
            put(PlaceContract.PlaceEntry.COLUMN_LOCATION, location)
            put(PlaceContract.PlaceEntry.COLUMN_CATEGORY, category)
        }
        db.insert(PlaceContract.PlaceEntry.TABLE_NAME, null, values)
    }

    fun insertSavedPlaceData(name: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(PlaceContract.SavedPlaceEntry.COLUMN_NAME, name)
        }
        db.insert(PlaceContract.SavedPlaceEntry.TABLE_NAME, null, values)
    }

    fun readPlaceData(): Cursor {
        val rDb = this.readableDatabase
        return rDb.rawQuery("SELECT * FROM ${PlaceContract.PlaceEntry.TABLE_NAME};", null)
    }

    fun readPlaceDataWithSamedCategory(category: String): Cursor {
        val rDb = this.readableDatabase
        return rDb.rawQuery(
            "SELECT * FROM ${PlaceContract.PlaceEntry.TABLE_NAME} WHERE ${PlaceContract.PlaceEntry.COLUMN_CATEGORY} = '${category}';",
            null
        )
    }

    fun readSavedPlaceData(): Cursor {
        val rDb = this.readableDatabase
        return rDb.rawQuery("SELECT * FROM ${PlaceContract.SavedPlaceEntry.TABLE_NAME};", null)
    }

    fun readSavedPlaceDataWithSamedName(name: String): Cursor {
        val rDb = this.readableDatabase
        return rDb.rawQuery(
            "SELECT * FROM ${PlaceContract.SavedPlaceEntry.TABLE_NAME} WHERE ${PlaceContract.SavedPlaceEntry.COLUMN_NAME} = '${name}';",
            null
        )
    }

    fun deleteSavedPlace(name: String) {
        val db = this.writableDatabase
        db.delete(PlaceContract.SavedPlaceEntry.TABLE_NAME, "${PlaceContract.SavedPlaceEntry.COLUMN_NAME} = ?", arrayOf(name))
    }

}