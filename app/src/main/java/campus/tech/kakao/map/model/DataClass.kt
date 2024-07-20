package campus.tech.kakao.map.model

import com.google.gson.annotations.SerializedName


/** @(serializedName( 받아올 get JSON ) val ( 내가 프로젝트에서 쓸 명칭 ) : 타입<Class> */
data class PlaceResponse(
    @SerializedName("documents") val documents: List<Document>,
    @SerializedName("meta") val meta: Meta
)

data class Document(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("category_group_code") val categoryGroupCode: String,
    @SerializedName("category_group_name") val categoryGroupName: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("distance") val distance: String,
    @SerializedName("id") val id: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("place_name") val placeName: String,
    @SerializedName("place_url") val placeUrl: String,
    @SerializedName("road_address_name") val roadAddressName: String,
    @SerializedName("x") val x: String,
    @SerializedName("y") val y: String
)

data class Meta(
    @SerializedName("is_end") val isEnd: Boolean,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("same_name") val sameName: Any,
    @SerializedName("total_count") val totalCount: Int
)