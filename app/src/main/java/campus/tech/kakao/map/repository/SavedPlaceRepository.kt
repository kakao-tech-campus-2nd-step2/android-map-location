package campus.tech.kakao.map.repository

import android.util.Log
import campus.tech.kakao.map.db.PlaceContract
import campus.tech.kakao.map.db.PlaceDBHelper
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace

class SavedPlaceRepository (val dbHelper: PlaceDBHelper){
    fun getAllSavedPlace() : List<SavedPlace>{
        val cursor = dbHelper.readSavedPlaceData()
        val placeList = mutableListOf<SavedPlace>()

        while (cursor.moveToNext()) {
            val place = SavedPlace(
                cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceContract.SavedPlaceEntry.COLUMN_NAME)
                )
            )
            Log.d("readData", "이름 = ${place.name}")
            placeList.add(place)
        }

        cursor.close()
        return placeList
    }

    fun writePlace(place: Place){
        val cursor = dbHelper.readSavedPlaceDataWithSamedName(place.name)
        if (cursor.moveToFirst()) {
            Log.d("testt", "데이터 중복")
            // 입력의 시간순대로 정렬되기 떄문에 레코드 삭제후 다시 집어넣기
            dbHelper.deleteSavedPlace(place.name)
            dbHelper.insertSavedPlaceData(place.name)
        } else {
            dbHelper.insertSavedPlaceData(place.name)
        }
    }

    fun deleteSavedPlace(savedPlace: SavedPlace){
        dbHelper.deleteSavedPlace(savedPlace.name)
    }
}