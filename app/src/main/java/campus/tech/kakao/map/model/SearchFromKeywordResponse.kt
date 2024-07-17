package campus.tech.kakao.map.model

import com.google.gson.annotations.SerializedName

data class SearchFromKeywordResponse(
    val documents: List<LocationDto>
)
data class LocationDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("place_name")
    val title: String,
    @SerializedName("category_group_name")
    val category: String,
    @SerializedName("address_name")
    val address: String
)
