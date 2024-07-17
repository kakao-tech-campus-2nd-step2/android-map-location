package campus.tech.kakao.map.model

data class Place (
    val name: String,
    val address: String,
    val category: String = "",
    val longitude: String = "",
    val latitude: String = ""
)