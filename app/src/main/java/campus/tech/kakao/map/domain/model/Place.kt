package campus.tech.kakao.map.domain.model

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("id") var id: String,
    @SerializedName("place_name") var place: String,
    @SerializedName("address_name") var address: String,
    @SerializedName("category_name")var category: String
)
