package campus.tech.kakao.map.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SearchPlace(
    var documents: List<PlaceInfo>
)

@Parcelize
data class PlaceInfo(
    val place_name: String,
    val category_group_name: String,
    val road_address_name: String,
    val x: String,
    val y: String
) : Parcelable