package campus.tech.kakao.map.domain.model

data class ResultSearchKeyword(
    var documents: List<Place>,
    val meta: PlaceMeta
)


