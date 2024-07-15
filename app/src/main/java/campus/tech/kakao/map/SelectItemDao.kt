package campus.tech.kakao.map

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class SelectItemDao(private val wDb: SQLiteDatabase, private val rDb: SQLiteDatabase) {

    fun insertSelectItem(name: String, address: String, category: String, id: String) {
        val values = ContentValues()
        values.put(SelectItemDB.TABLE_COLUMN_NAME, name)
        values.put(SelectItemDB.TABLE_COLUMN_ADDRESS, address)
        values.put(SelectItemDB.TABLE_COLUMN_CATEGORY, category)
        values.put(SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID, id)

        wDb.insert(SelectItemDB.TABLE_NAME, null, values)
    }

    fun deleteSelectItem(id: String) {
        wDb.delete(
            SelectItemDB.TABLE_NAME,
            "${SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID}=?",
            arrayOf(id)
        )
    }

    fun makeAllSelectItemList(): MutableList<KakaoMapItem> {
        val cursor = rDb.rawQuery(
            "Select * from ${SelectItemDB.TABLE_NAME} order by ${SelectItemDB.TABLE_COLUMN_ID} desc",
            null
        )
        val selectItemList = mutableListOf<KakaoMapItem>()
        while (cursor.moveToNext()) {
            selectItemList.add(
                KakaoMapItem(
                    cursor.getString(cursor.getColumnIndexOrThrow(SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SelectItemDB.TABLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SelectItemDB.TABLE_COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SelectItemDB.TABLE_COLUMN_CATEGORY))
                )
            )
        }
        cursor.close()

        return selectItemList
    }

    fun checkItemInDB(id : String) : Boolean {
        val cursor = rDb.rawQuery(
            "Select * from ${SelectItemDB.TABLE_NAME} where ${SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID} = ?",
            arrayOf(id)
        )
        if(cursor.getCount() > 0) {
            cursor.close()
            return true
        } else {
            cursor.close()
            return false
        }
    }
}