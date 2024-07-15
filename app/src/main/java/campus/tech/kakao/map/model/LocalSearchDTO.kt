package campus.tech.kakao.map.model

data class LocalSearchResponse(
    val documents: List<LocalSearchDocument>,
    val meta: LocalSearchMeta
)

data class LocalSearchDocument(
    val address_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val category_name: String,
    val distance: String,
    val id: String,
    val phone: String,
    val place_name: String,
    val place_url: String,
    val road_address_name: String,
    val x: String,
    val y: String
)

data class LocalSearchMeta(
    val is_end: Boolean,
    val pageable_count: Int,
    val same_name: LocalSearchSameName,
    val total_count: Int
)

data class LocalSearchSameName(
    val keyword: String,
    val region: List<String>,
    val selected_region: String
)
