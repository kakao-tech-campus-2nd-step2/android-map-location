package campus.tech.kakao.map.model

import com.google.gson.annotations.SerializedName

data class ResultSearch(
    val documents: List<Place>
)

data class Place(
    @SerializedName("place_name") val name : String,
    @SerializedName("road_address_name") val location : String?,
    @SerializedName("category_group_name") val category : String?
)
