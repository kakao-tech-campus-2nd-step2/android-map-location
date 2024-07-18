package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import androidx.core.database.getIntOrNull
import androidx.core.view.isVisible
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PlaceRepository(context: Context) {
    private val dbHelper = PlaceDbHelper(context)

    fun insertLog(place: Place) {
        val db = dbHelper.writableDatabase

        try {
            val cursor = db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(BaseColumns._ID),
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category.category),
                null,
                null,
                null
            )

            try {
                if (cursor.moveToFirst()) {
                    Log.d("PlaceRepository", "Place already exists: ${place.name}")
                } else {
                    val values = ContentValues().apply {
                        put(MyPlaceContract.Research.COLUMN_NAME, place.name)
                        put(MyPlaceContract.Research.COLUMN_IMG, place.img)
                        put(MyPlaceContract.Research.COLUMN_LOCATION, place.location)
                        put(MyPlaceContract.Research.COLUMN_CATEGORY, place.category.category)
                    }

                    val newRowId = db.insert(MyPlaceContract.Research.TABLE_NAME, null, values)
                    if (newRowId == -1L) {
                        Log.e("PlaceRepository", "Failed to insert row for ${place.name}")
                    } else {
                        Log.d("PlaceRepository", "Successfully inserted row for ${place.name}")
                    }
                }
            } finally {
                cursor.close()
            }
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error inserting row: ${e.message}")
        } finally {
            db.close()
        }
    }

    fun insertData2ResultView(query: String, callback: (List<Place>) -> Unit){
        var resultList: MutableList<Place>
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
                        resultList = (documentList.map {
                            val category = PlaceCategory.fromCategory(query)
                            Place(img = category.imgId, name = it.placeName, location = it.addressName, category = category)
                        } ?: emptyList<Place>()).toMutableList()
                        callback(resultList) // = updateRecyclerView(resultList)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        callback(emptyList())
                    }
                }

                override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                    Log.d("KakaoAPI", "Failure: ${t.message}")
                }
            })
    }

    fun hasResearchEntries() : Boolean {
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.rawQuery("SELECT COUNT(*) FROM ${MyPlaceContract.Research.TABLE_NAME}", null)
            try {
                return if (cursor.moveToFirst()) {
                    val count = cursor.getIntOrNull(0) ?: 0
                    count > 0
                } else {
                    false
                }
            } finally {
                cursor.close()
            }
        } finally {
            db.close()
        }
    }

    fun getResearchEntries(): List<Place> {
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(
                    MyPlaceContract.Research.COLUMN_IMG,
                    MyPlaceContract.Research.COLUMN_NAME,
                    MyPlaceContract.Research.COLUMN_LOCATION,
                    MyPlaceContract.Research.COLUMN_CATEGORY
                ),
                null, null, null, null, null
            )

            val researchList = mutableListOf<Place>()
            try {
                while (cursor.moveToNext()) {
                    var img = cursor.getInt(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_IMG))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_NAME))
                    val location = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_LOCATION))
                    val categoryDisplayName = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_CATEGORY))
                    val category = PlaceCategory.fromCategory(categoryDisplayName)
                    val place = Place(img = img, name = name, location = location, category = category)
                    researchList.add(place)
                }
            } finally {
                cursor.close()
            }
            return researchList
        } finally {
            db.close()
        }
    }

    fun deleteResearchEntry(place: Place) {
        val db = dbHelper.writableDatabase
        try {
            db.delete(
                MyPlaceContract.Research.TABLE_NAME,
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category.category)
            )
            Log.d("PlaceRepository", "Successfully deleted row for ${place.name}")
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error deleting row: ${e.message}")
        } finally {
            db.close()
        }
    }

    object RetrofitObject {
        val retrofitService: KakaoApiService by lazy { Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoApiService::class.java)
        }
    }
}