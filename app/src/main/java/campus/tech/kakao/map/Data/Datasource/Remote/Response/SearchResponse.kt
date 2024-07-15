package campus.tech.kakao.map.Data.Datasource.Remote.Response

import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.Domain.Model.PlaceCategory
import campus.tech.kakao.map.Domain.Model.PlaceCategory.Companion.groupCodeToPlaceCategory
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

data class SearchResponse(
    val documents: List<Document>?,
    val meta: Meta?
) : Response

data class SameName(
    @SerializedName("keyword") val keyword: String?,
    @SerializedName("region") val region: String?,
    @SerializedName("selected_region") val selectedRegion: String?
)

data class Meta(
    @SerializedName("is_end") val isEnd: Boolean?,
    @SerializedName("pageable_count") val pageableCount: Int?,
    @SerializedName("same_name") val sameName: SameName?,
    @SerializedName("total_count") val totalCount: Int?
)

data class Document(
    @SerializedName("address_name") val addressName: String?,
    @SerializedName("category_group_code") val categoryGroupCode: String?,
    @SerializedName("category_group_name") val categoryGroupName: String?,
    @SerializedName("category_name") val categoryName: String?,
    @SerializedName("distance") val distance: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("place_name") val placeName: String?,
    @SerializedName("place_url") val placeUrl: String?,
    @SerializedName("road_address_name") val roadAddressName: String?,
    @SerializedName("x") val x: String?,
    @SerializedName("y") val y: String?
)
fun Document.toVO() : Place {
    return Place(
        this.placeName ?: "Unknown",
        this.addressName ?: "Unknown",
        groupCodeToPlaceCategory(this.categoryGroupCode ?: "Unknown")
    )
}
