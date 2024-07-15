package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class KakaoMapItem(
    val id: String,
    val name: String,
    val address: String,
    val category: String
)

object SelectItemDB : BaseColumns {
    const val TABLE_NAME = "selectItem"
    const val TABLE_COLUMN_ID = "id"
    const val TABLE_COLUMN_NAME = "name"
    const val TABLE_COLUMN_ADDRESS = "address"
    const val TABLE_COLUMN_CATEGORY = "category"
    const val TABLE_COLUMN_MAP_ITEM_ID = "mapItemID"
}

class KakaoMapItemDbHelper(context: Context) : SQLiteOpenHelper(context, "mapItem.db", null, 1) {
    private val wDb = writableDatabase
    private val rDb = readableDatabase
    val selectItemDao = SelectItemDao(wDb, rDb)
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE ${SelectItemDB.TABLE_NAME} (" +
                    "${SelectItemDB.TABLE_COLUMN_ID} Integer primary key autoincrement," +
                    "${SelectItemDB.TABLE_COLUMN_NAME} varchar(20) not null," +
                    "${SelectItemDB.TABLE_COLUMN_ADDRESS} varchar(40) not null," +
                    "${SelectItemDB.TABLE_COLUMN_CATEGORY} varchar(10) not null," +
                    "${SelectItemDB.TABLE_COLUMN_MAP_ITEM_ID} varchar(20) not null" +
                    ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${SelectItemDB.TABLE_NAME}")
        onCreate(db)
    }

    suspend fun searchKakaoMapItem(category: String): MutableList<KakaoMapItem> {
        val mapItemList = mutableListOf<KakaoMapItem>()

        val response = withContext(Dispatchers.IO) {
            retrofitService.requsetKakaoMap(query = category).execute()
        }
        if(response.isSuccessful) {
            val body = response.body()
            //val maxPage = body?.meta?.pageable_count ?: 1
            val maxPage = 3
            for(i in 1..maxPage) {
                val responseEachPage = withContext(Dispatchers.IO) {
                    retrofitService.requsetKakaoMap(query = category, page = i).execute()
                }
                responseEachPage.body()?.documents?.forEach {
                    mapItemList.add(
                        KakaoMapItem(it.id, it.place_name, it.address_name, it.category_group_name)
                    )
                }

            }
        }
        return mapItemList
    }
}