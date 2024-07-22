package campus.tech.kakao.map.domain

import campus.tech.kakao.map.data.CategoryGroupCode

object CategoryGroupCodes {
    val categories = listOf(
        CategoryGroupCode("MT1", "대형마트"),
        CategoryGroupCode("CS2", "편의점"),
        CategoryGroupCode("PS3", "어린이집, 유치원"),
        CategoryGroupCode("SC4", "학교"),
        CategoryGroupCode("AC5", "학원"),
        CategoryGroupCode("PK6", "주차장"),
        CategoryGroupCode("OL7", "주유소, 충전소"),
        CategoryGroupCode("SW8", "지하철역"),
        CategoryGroupCode("BK9", "은행"),
        CategoryGroupCode("CT1", "문화시설"),
        CategoryGroupCode("AG2", "중개업소"),
        CategoryGroupCode("PO3", "공공기관"),
        CategoryGroupCode("AT4", "관광명소"),
        CategoryGroupCode("AD5", "숙박"),
        CategoryGroupCode("FD6", "음식점"),
        CategoryGroupCode("CE7", "카페"),
        CategoryGroupCode("HP8", "병원"),
        CategoryGroupCode("PM9", "약국")
    )

    val categoryMap = categories.associateBy({ it.name }, { it.code })

    fun getCodeByName(name: String): String? {
        return categoryMap[name]
    }
}