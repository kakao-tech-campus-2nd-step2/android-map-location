package campus.tech.kakao.map.dBHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.dataContract.RecentDataContract

//version 2: DB에 time 컬럼을 추가함
//version 3" DB에 address 컬럼을 추가함
class RecentDBHelper(context: Context) : SQLiteOpenHelper(context, "RecentData.db", null, 3) {
    override fun onCreate(db: SQLiteDatabase?) {
        createRecentDataTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${RecentDataContract.TABLE_NAME}")
        createRecentDataTable(db)
    }

    private fun createRecentDataTable(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE ${RecentDataContract.TABLE_NAME} (" +
                    "${RecentDataContract.TABLE_COLUMN_NAME} varchar(30) NOT NULL, " +
                    "${RecentDataContract.TABLE_COLUMN_ADDRESS} varchar(50) NOT NULL, " +
                    "${RecentDataContract.TABLE_COLUMN_TIME} integer NOT NULL);"
        )
    }
}