package campus.tech.kakao.map.model

data class KakaoMapProductResponse(
    val documents: List<Document>
)

data class Document(
    val place_name: String,
    val address_name: String,
    val category_group_name: String,
    val x: String,
    val y: String
)
