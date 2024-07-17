package campus.tech.kakao.map.Model

data class SearchResult(
    val documents: List<Place>
)

data class Place(
    val place_name: String,
    val address_name: String,
    val category_group_name: String,
)