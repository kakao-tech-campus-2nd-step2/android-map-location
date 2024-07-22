package campus.tech.kakao.map.data.repository

import androidx.datastore.core.Serializer
import campus.tech.kakao.map.LocationDataProto.LocationData
import campus.tech.kakao.map.model.Location
import java.io.InputStream
import java.io.OutputStream

object LocationSerializer : Serializer<Location> {
    override val defaultValue: Location =
        Location(
            name = "부산대 컴공관",
            latitude = 35.230934,
            longitude = 129.082476,
            address = "부산광역시 금정구 부산대학로 63번길 2",
        )

    override suspend fun readFrom(input: InputStream): Location {
        return try {
            val locationData = LocationData.parseFrom(input)
            locationData.toLocation()
        } catch (exception: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: Location,
        output: OutputStream,
    ) {
        val locationData = t.toLocationData()
        locationData.writeTo(output)
    }

    private fun LocationData.toLocation(): Location {
        return Location(
            name = this.name,
            latitude = this.latitude,
            longitude = this.longitude,
            address = this.address,
        )
    }

    private fun Location.toLocationData(): LocationData {
        return LocationData.newBuilder()
            .setName(this.name)
            .setLatitude(this.latitude)
            .setLongitude(this.longitude)
            .setAddress(this.address)
            .build()
    }
}
