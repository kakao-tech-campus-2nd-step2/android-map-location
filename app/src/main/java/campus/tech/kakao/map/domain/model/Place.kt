package campus.tech.kakao.map.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Place(
    @SerializedName("id") var id: String,
    @SerializedName("place_name") var place: String,
    @SerializedName("address_name") var address: String,
    @SerializedName("category_name")var category: String,
    @SerializedName("x") var xPos: String,
    @SerializedName("y") var yPos: String
): Serializable

