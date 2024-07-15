package campus.tech.kakao.map.model

data class SearchPlace(
    var documents: List<PlaceInfo>
)

data class PlaceInfo(
    val place_name: String,
    val category_group_name: String,
    val road_address_name: String,
)