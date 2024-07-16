package campus.tech.kakao.map.retrofit

import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("place_name") val name: String,
    @SerializedName("category_name") val category: String,
    @SerializedName("address_name") val address: String,
    @SerializedName("category_group_code") val categoryCode: String,
    var categoryDescription: String? = null,
    var categoryTail: String? = null
)

data class MapSearchResponse(
    @SerializedName("documents") val documents: List<Document>
)

