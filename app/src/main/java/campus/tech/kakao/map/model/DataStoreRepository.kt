package campus.tech.kakao.map.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val latitudeKey = doublePreferencesKey("latitude")
        private val longitudeKey = doublePreferencesKey("longitude")
    }

    suspend fun saveLocation(latitude: Double, longitude: Double) {
        dataStore.edit {
            it[latitudeKey] = latitude
            it[longitudeKey] = longitude
        }
    }

    fun loadLocation(): Flow<LatLng?> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        it[latitudeKey]?.let { latitude ->
            it[longitudeKey]?.let { longitude ->
                LatLng.from(latitude, longitude)
            }
        }
    }
}