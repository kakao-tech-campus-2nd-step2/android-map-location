package campus.tech.kakao.map

data class Location(
    val id: String,
    val name: String,
    val category: String,
    val address: String,
    val x: Double,
    val y: Double
) {
    companion object {
        const val CAFE: String = "카페"
        const val PHARMACY: String = "약국"
        const val RESTAURANT: String = "식당"
        const val NORMAL: String = "일반"
    }
}