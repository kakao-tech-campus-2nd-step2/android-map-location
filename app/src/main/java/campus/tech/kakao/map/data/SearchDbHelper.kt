package campus.tech.kakao.map.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import campus.tech.kakao.map.domain.model.SearchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class SearchDbHelper(context: Context) : SQLiteOpenHelper(context, "searchDb", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE ${SearchData.TABLE_NAME} (" +
                    "   ${SearchData.TABLE_COLUMN_NAME} varchar(255)," +
                    "   ${SearchData.TABLE_COLUMN_ADDRESS} varchar(255)," +
                    "   ${SearchData.TABLE_COLUMN_CATEGORY} varchar(255)," +
                    "   ${SearchData.TABLE_COLUMN_XCOORDINATE} double," +
                    "   ${SearchData.TABLE_COLUMN_YCOORDINATE} double" +
                    ");"
        )

        db?.execSQL(
            "CREATE TABLE ${SearchData.SAVED_SEARCH_TABLE_NAME} (" +
                    "   ${SearchData.SAVED_SEARCH_COLUMN_NAME} varchar(255)" +
                    ");"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${SearchData.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${SearchData.SAVED_SEARCH_TABLE_NAME}")
        onCreate(db)
    }

    fun getAllSavedWords(): List<String> {
        val db = readableDatabase
        val cursor = db.query(
            SearchData.SAVED_SEARCH_TABLE_NAME,
            arrayOf(SearchData.SAVED_SEARCH_COLUMN_NAME),
            null,
            null,
            null,
            null,
            null
        )
        val savedWords = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                savedWords.add(getString(getColumnIndexOrThrow(SearchData.SAVED_SEARCH_COLUMN_NAME)))
            }
        }
        cursor.close()
        return savedWords
    }

    suspend fun fetchApi(authorization: String) {
        withContext(Dispatchers.IO) {

            val retrofitService = Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)

            val categoryGroupCodes = listOf("PM9", "CE7")
            val x = "127.05897078335246"
            val y = "37.506051888130386"
            val radius = 20000
            val format = "json"

            clearTable()

            for (categoryGroupCode in categoryGroupCodes) {
                try {
                    val response = retrofitService.requestProducts(
                        authorization,
                        format,
                        categoryGroupCode,
                        x,
                        y,
                        radius
                    ).awaitResponse()

                    if (response.isSuccessful) {
                        val kakaoData = response.body()
                        kakaoData?.documents?.forEach { document ->
                            val placeName = document.placeName
                            val addressName = document.addressName
                            val categoryGroupName = document.categoryGroupName
                            val xCoordinate = document.x.toDouble()
                            val yCoordinate = document.y.toDouble()

                            saveDb(
                                placeName,
                                addressName,
                                categoryGroupName,
                                xCoordinate,
                                yCoordinate
                            )
                        }
                    } else {
                        Log.e(
                            "Retrofit",
                            "API 요청 실패, 응답 코드: ${response.code()}, 메시지: ${response.message()}"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("Retrofit", "API 요청 실패, 네트워크 에러: ${e.message}")
                }
            }
        }
    }

    fun saveDb(
        placeName: String,
        addressName: String,
        categoryGroupName: String,
        xCoordinate: Double,
        yCoordinate: Double
    ) {
        val wDb = writableDatabase
        val values = ContentValues()

        values.put(SearchData.TABLE_COLUMN_NAME, placeName)
        values.put(SearchData.TABLE_COLUMN_ADDRESS, addressName)
        values.put(SearchData.TABLE_COLUMN_CATEGORY, categoryGroupName)
        values.put(SearchData.TABLE_COLUMN_XCOORDINATE, xCoordinate)
        values.put(SearchData.TABLE_COLUMN_YCOORDINATE, yCoordinate)
        wDb.insert(SearchData.TABLE_NAME, null, values)
        values.clear()

    }

    fun loadDb(): List<SearchData> {
        val rDb = readableDatabase
        val cursor = rDb.query(
            SearchData.TABLE_NAME,
            arrayOf(
                SearchData.TABLE_COLUMN_NAME,
                SearchData.TABLE_COLUMN_ADDRESS,
                SearchData.TABLE_COLUMN_CATEGORY,
                SearchData.TABLE_COLUMN_XCOORDINATE,
                SearchData.TABLE_COLUMN_YCOORDINATE
            ),
            null,
            null,
            null,
            null,
            null
        )

        val searchDataList = mutableListOf<SearchData>()

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(SearchData.TABLE_COLUMN_NAME))
                val address = getString(getColumnIndexOrThrow(SearchData.TABLE_COLUMN_ADDRESS))
                val category = getString(getColumnIndexOrThrow(SearchData.TABLE_COLUMN_CATEGORY))
                val x = getDouble(getColumnIndexOrThrow(SearchData.TABLE_COLUMN_XCOORDINATE))
                val y = getDouble(getColumnIndexOrThrow(SearchData.TABLE_COLUMN_YCOORDINATE))
                searchDataList.add(SearchData(name, address, category, x, y))
                Log.e("Retrofit", "SearchDataList 찾기: $searchDataList")
            }
        }
        cursor.close()
        return searchDataList
    }

    private fun clearTable() {
        val wDb = writableDatabase
        wDb.delete(SearchData.TABLE_NAME, null, null)
    }
}