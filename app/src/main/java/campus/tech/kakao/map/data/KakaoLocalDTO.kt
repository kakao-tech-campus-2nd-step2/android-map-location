package campus.tech.kakao.map.data

import com.google.gson.annotations.SerializedName

data class SearchResult(
    val meta: PlaceMeta,
    val documents: List<PlaceDocument>
)

data class PlaceMeta(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("same_name")
    val sameName: SameName
)

data class SameName(
    val region: List<String>,
    val keyword: String,
    @SerializedName("selected_region")
    val selectedRegion: String
)

data class PlaceDocument(
    val id: String,
    @SerializedName("place_name")
    val placeName: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_group_code")
    val categoryGroupCode: String,
    @SerializedName("category_group_name")
    val categoryGroupName: String,
    val phone: String,
    @SerializedName("address_name")
    val addressName: String,
    @SerializedName("road_address_name")
    val roadAddressName: String,
    val x: String,
    val y: String,
    @SerializedName("place_url")
    val placeUrl: String,
    val distance: String
)

data class CategoryGroupCode(
    val code: String,
    val name: String
)