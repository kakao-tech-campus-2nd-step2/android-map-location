package campus.tech.kakao.map.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import campus.tech.kakao.map.database.SelectItemDB
import campus.tech.kakao.map.database.SelectMapItem

class SelectItemDao(private val wDb: SQLiteDatabase, private val rDb: SQLiteDatabase) {

    fun insertSelectItem(name: String, id: String) {
        val values = ContentValues()
        values.put(SelectItemDB.TABLE_COLUMN_NAME, name)
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

    fun makeAllSelectItemList(): MutableList<SelectMapItem> {
        val cursor = rDb.rawQuery(
            "Select * from ${SelectItemDB.TABLE_NAME} order by ${SelectItemDB.TABLE_COLUMN_ID} desc",
            null
        )
        val selectItemList = mutableListOf<SelectMapItem>()
        while (cursor.moveToNext()) {
            selectItemList.add(
                SelectMapItem(
                    cursor.getString(cursor.getColumnIndexOrThrow(SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SelectItemDB.TABLE_COLUMN_NAME))
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

    fun checkTableExist() : Boolean {
        val cursor = rDb.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name = '${SelectItemDB.TABLE_NAME}'",
            null
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