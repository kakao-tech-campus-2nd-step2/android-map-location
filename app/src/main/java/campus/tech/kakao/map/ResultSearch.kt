package campus.tech.kakao.map

data class ResultSearch(
    var documents: List<Placeitem>
)

data class Placeitem(
    var place_name: String,
    var category_group_name: String,
    var road_address_name: String
)
