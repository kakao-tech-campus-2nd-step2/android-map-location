package campus.tech.kakao.map.Domain.Model

data class Place(
    val id: Int,
    val name: String,
    var address: String? = null,
    var category: PlaceCategory? = null,
    var x : Double,
    var y : Double,
)
