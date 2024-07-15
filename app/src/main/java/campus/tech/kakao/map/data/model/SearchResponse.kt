package campus.tech.kakao.map.data.model

data class SearchResponse(
    val meta: MetaEntity,
    val documents: List<DocumentEntity>
)