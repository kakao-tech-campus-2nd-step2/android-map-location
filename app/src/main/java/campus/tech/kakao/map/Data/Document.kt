package campus.tech.kakao.map.Data

import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("category_group_name") val categoryName: String,
    @SerializedName("id") val id: String,
    @SerializedName("address_name") val addressName: String,
    @SerializedName("place_name") val placeName: String?
)