package campus.tech.kakao.map

import android.content.ContentValues
import android.util.Log
import androidx.core.database.getIntOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainModel(private val application: MyApplication) {
    private val dbHelper = PlaceDbHelper(application)
    private var placeList = mutableListOf<Place>()
    private var logList = mutableListOf<Place>()

    //local API에 필요한 Retrofit_클라이언트 생성
    object RetrofitObject {
        val retrofitService: KakaoApiService by lazy { Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoApiService::class.java)
        }
    }

    fun callKakao(query: String, callback: (List<Place>) -> Unit){
        val apiKey = "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY
        val categoryGroupCode: String = when (query) {
            "카페" -> "CE7"
            "약국" -> "PM9"
            else -> null
        } ?: return

        RetrofitObject.retrofitService.getPlace(apiKey, categoryGroupCode)
            .enqueue(object : Callback<KakaoResponse> {
                override fun onResponse(
                    call: Call<KakaoResponse>,
                    response: Response<KakaoResponse>
                ) {
                    if (response.isSuccessful) {
                        val documentList = response.body()?.documents ?: emptyList()
                        placeList = documentList.map {
                            val category = PlaceCategory.fromCategory(query)
                            Place(img = category.imgId, name = it.placeName, location = it.addressName, category = category)
                        }.toMutableList()
                        callback(placeList)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.d("KakaoAPI", errorBody.toString())
                        callback(emptyList())
                        placeList = emptyList<Place>().toMutableList()
                    }
                }

                override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                    Log.d("KakaoAPI", "Failure: ${t.message}")
                }
            })
    }

    // Click_DB에 클릭한 Place1 객체 넣기 (이미 클릭해서 진입한 거면 필요없음)
    fun insertLog(place: Place) {
        dbHelper.writableDatabase.use { db ->
            db.rawQuery(
                "SELECT 1 FROM ${MyPlaceContract.Research.TABLE_NAME} WHERE ${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.location, place.category.category)
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    Log.d("PlaceRepository", "Place already exists: ${place.name}")
                } else {
                    val values = ContentValues().apply {
                        put(MyPlaceContract.Research.COLUMN_NAME, place.name)
                        put(MyPlaceContract.Research.COLUMN_IMG, place.img)
                        put(MyPlaceContract.Research.COLUMN_LOCATION, place.location)
                        put(MyPlaceContract.Research.COLUMN_CATEGORY, place.category.category)
                    }
                    db.insert(MyPlaceContract.Research.TABLE_NAME, null, values)
                    logList.add(place)
                }
            }
        }
    }

    // tabRecyclerView를 표시할지 여부를 정하기 위해 <- Click_DB에 클릭한 기록이 있는지 확인
    fun hasAnyClick() : Boolean {
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
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category.category)
            )
        }
        logList.remove(place)
    }

    // 처음 시작시 이전에 저장된 Click_DB 값들을 가져옴 -> 초기 researchList 업데이트
    fun getResearchLogs() {
        dbHelper.readableDatabase.use { db ->
            db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(
                    MyPlaceContract.Research.COLUMN_IMG,
                    MyPlaceContract.Research.COLUMN_NAME,
                    MyPlaceContract.Research.COLUMN_LOCATION,
                    MyPlaceContract.Research.COLUMN_CATEGORY
                ),
                null, null, null, null, null
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    val img = cursor.getInt(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_IMG))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_NAME))
                    val location = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_LOCATION))
                    val categoryDisplayName = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_CATEGORY))
                    val category = PlaceCategory.fromCategory(categoryDisplayName)
                    val place = Place(img = img, name = name, location = location, category = category)
                    logList.add(place)
                }
            }
        }
    }

    // viewModel에 데이터 알려주기
    fun getLogList() = logList
}