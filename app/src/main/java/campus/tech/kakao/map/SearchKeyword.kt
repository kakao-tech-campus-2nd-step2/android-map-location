package campus.tech.kakao.map

import android.provider.BaseColumns

data class SearchKeyword(
    val searchKeyword: String
)

object SearchKeywordEntry : BaseColumns {
    const val TABLE_NAME = "search_keyword"
    const val SEARCH_KEYWORD = "search_keyword"
}