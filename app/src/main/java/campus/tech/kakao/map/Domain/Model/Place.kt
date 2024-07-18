package campus.tech.kakao.map.Domain.Model

data class Place(val name: String, var address: String? = null, var category: PlaceCategory? = null)
