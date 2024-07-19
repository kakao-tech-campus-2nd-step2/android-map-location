package campus.tech.kakao.map.model

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepository(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "last_location")
        private val latitudeKey = doublePreferencesKey("latitude")
        private val longitudeKey = doublePreferencesKey("longitude")
    }

    suspend fun saveLocation(latitude: Double, longitude: Double) {
        context.dataStore.edit {
            it[latitudeKey] = latitude
            it[longitudeKey] = longitude
        }
    }

    fun loadLocation(): Flow<LatLng?> = context.dataStore.data.catch { exception ->
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