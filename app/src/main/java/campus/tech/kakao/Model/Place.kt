package campus.tech.kakao.Model;

import com.google.gson.annotations.SerializedName


data class Place(
    @SerializedName("place_name") val placeName: String,
    @SerializedName("road_address_name") val roadAddressName: String?,
    @SerializedName("category_group_code") val categoryName: String?
)
