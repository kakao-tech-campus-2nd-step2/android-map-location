package campus.tech.kakao.map.dataRepository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import campus.tech.kakao.map.dBHelper.RecentDBHelper
import campus.tech.kakao.map.dataContract.RecentDataContract
import campus.tech.kakao.map.data.RecentSearchData

class RecentDataRepository(context: Context) {
    private val db: RecentDBHelper = RecentDBHelper(context)
    private val wDb = db.writableDatabase
    private val rDb = db.readableDatabase

    fun insertSearchData(data: RecentSearchData) {
        val cv = ContentValues().apply {
            put(RecentDataContract.TABLE_COLUMN_NAME, data.name)
            put(RecentDataContract.TABLE_COLUMN_ADDRESS, data.address)
            put(RecentDataContract.TABLE_COLUMN_TIME, data.time)
        }
        wDb.insert(RecentDataContract.TABLE_NAME, null, cv)
    }

    fun getRecentSearchDataList(): List<RecentSearchData> {
        val cursor: Cursor = rDb.query(
            RecentDataContract.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${RecentDataContract.TABLE_COLUMN_TIME} DESC"
        )
        val recentDataList = mutableListOf<RecentSearchData>()

        while (cursor.moveToNext()) {
            val name =
                cursor.getString(cursor.getColumnIndexOrThrow(RecentDataContract.TABLE_COLUMN_NAME))
            val address =
                cursor.getString(cursor.getColumnIndexOrThrow(RecentDataContract.TABLE_COLUMN_ADDRESS))
            val time =
                cursor.getString(cursor.getColumnIndexOrThrow(RecentDataContract.TABLE_COLUMN_TIME))

            recentDataList.add(RecentSearchData(name, address, time.toLong()))
        }
        cursor.close()

        return recentDataList
    }

    fun deleteSearchData(data: String, address: String) {
        wDb.delete(
            RecentDataContract.TABLE_NAME,
            "${RecentDataContract.TABLE_COLUMN_NAME} = ? AND ${RecentDataContract.TABLE_COLUMN_ADDRESS} = ?",
            arrayOf(data, address)
        )
    }

    fun updateTime(data: RecentSearchData) {
        val cv = ContentValues().apply {
            put(RecentDataContract.TABLE_COLUMN_TIME, data.time)
        }

        wDb.update(
            RecentDataContract.TABLE_NAME,
            cv,
            "${RecentDataContract.TABLE_COLUMN_NAME} = ?",
            arrayOf(data.name)
        )
    }
}