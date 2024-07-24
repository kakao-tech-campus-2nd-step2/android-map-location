package campus.tech.kakao.map.model

data class Location(
    val title: String,
    val address: String,
    val category: String,
    val longitude: Double,
    val latitude: Double
){
    companion object {
        fun LocationDto.toLocation(): Location {
            return Location(title, address, category, x.toDouble(), y.toDouble())

        }
    }
}