package campus.tech.kakao.map.repository

import android.content.ContentValues
import android.util.Log
import androidx.core.database.getIntOrNull
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.base.MyApplication
import campus.tech.kakao.map.data.db.MyPlaceContract
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.data.db.PlaceDbHelper
import campus.tech.kakao.map.data.remote.model.KakaoResponse
import campus.tech.kakao.map.data.remote.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceRepository(private val application: MyApplication) {
    private val dbHelper = PlaceDbHelper(application)
    private var logList = mutableListOf<Place>()

    fun searchPlaces(query: String, callback: (List<Place>) -> Unit){
        val apiKey = "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY
        val retrofitService = RetrofitClient.retrofitService

        retrofitService.getPlace(apiKey, query)
            .enqueue(object : Callback<KakaoResponse> {
                override fun onResponse(
                    call: Call<KakaoResponse>,
                    response: Response<KakaoResponse>
                ) {
                    if (response.isSuccessful) {
                        val documentList = response.body()?.documents ?: emptyList()
                        val placeList = documentList.map {
                            Place(
                                img = R.drawable.location,
                                name = it.placeName,
                                location = it.addressName,
                                category = it.categoryGroupName,
                                x = it.x,
                                y = it.y)
                        }
                        callback(placeList)
                    } else {
                        Log.d("KakaoAPI", response.errorBody()?.string().toString())
                        callback(emptyList())
                    }
                }

                override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                    Log.d("KakaoAPI", "Failure: ${t.message}")
                    callback(emptyList())
                }
            })
    }

    fun insertLog(place: Place) {
        dbHelper.writableDatabase.use { db ->
            db.rawQuery(
                "SELECT 1 FROM ${MyPlaceContract.Research.TABLE_NAME} WHERE ${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ? AND ${MyPlaceContract.Research.COLUMN_X} = ? AND ${MyPlaceContract.Research.COLUMN_Y} = ?",
                arrayOf(place.name, place.location, place.category, place.x, place.y)
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    Log.d("PlaceRepository", "Place already exists: ${place.name}")
                } else {
                    val values = ContentValues().apply {
                        put(MyPlaceContract.Research.COLUMN_NAME, place.name)
                        put(MyPlaceContract.Research.COLUMN_IMG, place.img)
                        put(MyPlaceContract.Research.COLUMN_LOCATION, place.location)
                        put(MyPlaceContract.Research.COLUMN_CATEGORY, place.category)
                        put(MyPlaceContract.Research.COLUMN_X, place.x)
                        put(MyPlaceContract.Research.COLUMN_Y, place.y)
                    }
                    db.insert(MyPlaceContract.Research.TABLE_NAME, null, values)
                    logList.add(place)
                }
            }
        }
    }

    // tabRecyclerView를 표시할지 여부를 정하기 위해 <- Click_DB에 클릭한 기록이 있는지 확인
    fun hasStoredLogs() : Boolean {
        dbHelper.readableDatabase.use {
            it.rawQuery("SELECT COUNT(*) FROM ${MyPlaceContract.Research.TABLE_NAME}", null).use { cursor ->
                return if (cursor.moveToFirst()) {
                    val count = cursor.getIntOrNull(0) ?: 0
                    count > 0
                } else {
                    false
                }
            }
        }
    }

    // X버튼 클릭시 해당클릭로그 ⓐ Click_DB, ⓑ 로컬리스트에서 삭제
    fun deleteResearchEntry(place: Place) {
        dbHelper.writableDatabase.use {
            it.delete(
                MyPlaceContract.Research.TABLE_NAME,
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ? AND ${MyPlaceContract.Research.COLUMN_X} = ? AND ${MyPlaceContract.Research.COLUMN_Y} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category, place.x, place.y)
            )
        }
        logList.remove(place)
    }

    // 처음 시작시 이전에 저장된 Click_DB 값들을 가져옴 -> 초기 researchList 업데이트
    fun getResearchLogs(): List<Place> {
        dbHelper.readableDatabase.use { db ->
            db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(
                    MyPlaceContract.Research.COLUMN_IMG,
                    MyPlaceContract.Research.COLUMN_NAME,
                    MyPlaceContract.Research.COLUMN_LOCATION,
                    MyPlaceContract.Research.COLUMN_CATEGORY,
                    MyPlaceContract.Research.COLUMN_X,
                    MyPlaceContract.Research.COLUMN_Y
                ),
                null, null, null, null, null
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    val img = cursor.getInt(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_IMG))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_NAME))
                    val location = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_LOCATION))
                    val category = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_CATEGORY))
                    val x = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_X))
                    val y = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_Y))
                    val place = Place(img = img, name = name, location = location, category = category, x = x, y = y)
                    logList.add(place)
                }
            }
        }
        return logList
    }

    // viewModel에 데이터 알려주기
    fun getLogList() = logList
}