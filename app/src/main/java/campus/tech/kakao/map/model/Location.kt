package campus.tech.kakao.map.model

data class Location(
    val title: String,
    val address: String,
    val category: String
){
    companion object {
        fun LocationDto.toLocation(): Location {
            return Location(title, address, category)
        }
    }
}