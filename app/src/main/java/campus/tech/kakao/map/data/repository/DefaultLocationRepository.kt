package campus.tech.kakao.map.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import campus.tech.kakao.map.model.Location
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultLocationRepository
@Inject
constructor(
    private val dataStore: DataStore<Location>,
) : LocationRepository {
    override suspend fun saveLocation(location: Location) {
        dataStore.updateData {
            Location(
                name = location.name,
                latitude = location.latitude,
                longitude = location.longitude,
                address = location.address,
            )
        }
    }

    override suspend fun loadLocation(): Location {
        return try {
            dataStore.data
                .map { location ->
                    Location(
                        name = location.name,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        address = location.address,
                    )
                }
                .firstOrNull() ?: getDefaultLocation()
        } catch (exception: Exception) {
            Log.e("DefaultLocationRepository", "Failed to load location data", exception)
            getDefaultLocation()
        }
    }

    private fun getDefaultLocation(): Location {
        return Location(
            name = "부산대 컴공관",
            latitude = 35.230934,
            longitude = 129.082476,
            address = "부산광역시 금정구 부산대학로 63번길 2",
        )
    }
}
