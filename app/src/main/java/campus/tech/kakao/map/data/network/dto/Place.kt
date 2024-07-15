package campus.tech.kakao.map.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    @SerialName("id") val placeId: String = "",
    @SerialName("place_name") val placeName: String = "",
    @SerialName("address_name") val address: String = "",
    @SerialName("category_group_name") val category: String = "",
)
