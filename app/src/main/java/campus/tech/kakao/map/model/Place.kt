package campus.tech.kakao.map.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResultSearch(
    val documents: List<Place>
)
@Parcelize
data class Place(
    @SerializedName("place_name") val name : String,
    @SerializedName("road_address_name") val location : String?,
    @SerializedName("category_group_name") val category : String?,
    @SerializedName("x") val x: String? = "",
    @SerializedName("y") val y: String? = ""
) : Parcelable