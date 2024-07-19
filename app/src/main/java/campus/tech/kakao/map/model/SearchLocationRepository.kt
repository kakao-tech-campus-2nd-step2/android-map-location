package campus.tech.kakao.map.model

import android.content.ContentValues

class SearchLocationRepository(
    private val historyDbHelper: HistoryDbHelper,
    private val localSearchService: LocalSearchService
) {
    suspend fun searchLocation(category: String): List<Location> {
        val response = localSearchService.requestLocalSearch(query = category)
        return response.body()?.documents?.map {
            Location(
                name = it.place_name,
                address = it.address_name,
                category = it.category_group_name,
                latitude = it.y.toDouble(),
                longitude = it.x.toDouble()
            )
        } ?: emptyList()
    }

    fun addHistory(locationName: String) {
        if (isExistHistory(locationName)) {
            removeHistory(locationName)
        }

        val db = historyDbHelper.writableDatabase
        val historyValues = ContentValues()
        historyValues.put(HistoryContract.COLUMN_NAME, locationName)
        db.insert(HistoryContract.TABLE_NAME, null, historyValues)

        db.close()
    }

    fun getHistory(): List<String> {
        val db = historyDbHelper.readableDatabase
        val searchQuery = "SELECT * FROM ${HistoryContract.TABLE_NAME}"
        val cursor = db.rawQuery(searchQuery, null)

        val result = mutableListOf<String>()
        while (cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndexOrThrow(HistoryContract.COLUMN_NAME)))
        }

        cursor.close()
        db.close()
        return result.toList()
    }

    private fun isExistHistory(locationName: String): Boolean {
        val db = historyDbHelper.readableDatabase
        val searchQuery = "SELECT * FROM ${HistoryContract.TABLE_NAME} " +
                "WHERE ${HistoryContract.COLUMN_NAME} = '$locationName'"
        val cursor = db.rawQuery(searchQuery, null)

        val result = cursor.count > 0
        cursor.close()
        db.close()

        return result
    }

    fun removeHistory(locationName: String) {
        val db = historyDbHelper.writableDatabase
        val deleteQuery = "DELETE FROM ${HistoryContract.TABLE_NAME} " +
                "WHERE ${HistoryContract.COLUMN_NAME} = '$locationName'"
        db.execSQL(deleteQuery)
        db.close()
    }
}