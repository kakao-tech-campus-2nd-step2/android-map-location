package campus.tech.kakao.map

enum class PlaceCategory(val category: String, val imgId: Int) {
    CAFE("카페", R.drawable.cafe),
    PHARMACY("약국", R.drawable.hospital),
    OTHER("기타", R.drawable.location);

    companion object {
        fun fromCategory(category: String): PlaceCategory {
            return entries.find { it.category == category } ?: OTHER
        }
    }

}