package campus.tech.kakao.map.data.network

import campus.tech.kakao.map.data.network.dto.Place
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceResponse(
    @SerialName("documents") val documents: List<Place>,
)
