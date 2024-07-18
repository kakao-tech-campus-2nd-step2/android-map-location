package campus.tech.kakao.map

import com.google.gson.annotations.SerializedName

data class KakaoResponse(
    val meta: Meta,
    val documents: List<Document>
)

data class Meta(
    @SerializedName("same_name") val sameName: Any?,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("is_end") val isEnd: Boolean
)

data class Document(
    @SerializedName("place_name") val placeName: String,
    val distance: String?,
    @SerializedName("place_url") val placeUrl: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("address_name") val addressName: String,
    @SerializedName("road_address_name") val roadAddressName: String,
    val id: String,
    val phone: String?,
    @SerializedName("category_group_code") val categoryGroupCode: String,
    @SerializedName("category_group_name") val categoryGroupName: String,
    val x: String,
    val y: String
)