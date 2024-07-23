package campus.tech.kakao.map.dto

data class PlaceMeta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean,
    val same_name: RegionInfo
) // 검색 결과 메타데이터 클래스