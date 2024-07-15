package campus.tech.kakao.map.data.model

import com.google.gson.annotations.SerializedName

data class DocumentEntity(
    val id: String,
    @SerializedName("place_name")
    val placeName: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_group_code")
    val categoryGroupCode: String?,
    @SerializedName("category_group_name")
    val categoryGroupName: String,
    val phone: String?,
    @SerializedName("address_name")
    val addressName: String,
    @SerializedName("road_address_name")
    val roadAddressName: String?,
    val x: String,
    val y: String,
    @SerializedName("place_url")
    val placeUrl: String,
    val distance: String?
)