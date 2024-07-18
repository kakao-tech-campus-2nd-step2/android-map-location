package campus.tech.kakao.map.model

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepository(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "last_location_datastore")

        private val latitudeKey = doublePreferencesKey("latitude")
        private val longitudeKey = doublePreferencesKey("longitude")
    }

    suspend fun saveLocation(latitude: Double, longitude: Double) {
        context.dataStore.edit {
            it[latitudeKey] = latitude
            it[longitudeKey] = longitude
        }
    }

    fun loadLocation(): Flow<LatLng?> = context.dataStore.data.map {
        it[latitudeKey]?.let { latitude ->
            it[longitudeKey]?.let { longitude ->
                LatLng.from(latitude, longitude)
            }
        }
    }
}