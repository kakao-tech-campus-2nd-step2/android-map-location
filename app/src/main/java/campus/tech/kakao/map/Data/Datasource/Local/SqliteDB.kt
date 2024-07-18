package campus.tech.kakao.map.Data.Datasource.Local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.Domain.Model.PlaceContract


class SqliteDB(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(PlaceContract.PlaceEntry.SQL_CREATE_TABLE)
        db.execSQL(PlaceContract.FavoriteEntry.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(PlaceContract.PlaceEntry.SQL_DROP_TABLE)
        onCreate(db)
    }

}