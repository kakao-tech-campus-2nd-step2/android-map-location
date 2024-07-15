package campus.tech.kakao.map

import com.google.gson.annotations.SerializedName

data class SearchResults(
    @SerializedName("documents")
    val places: List<Place>
)

data class Place(
    val place_name: String,
    val category_name: String,
    val address_name: String
)