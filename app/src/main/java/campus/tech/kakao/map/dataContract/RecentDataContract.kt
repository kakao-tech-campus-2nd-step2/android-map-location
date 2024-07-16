package campus.tech.kakao.map.dataContract

import android.provider.BaseColumns

object RecentDataContract : BaseColumns {
    const val TABLE_NAME = "RecentData"
    const val TABLE_COLUMN_NAME = "name"
    const val TABLE_COLUMN_ADDRESS = "address"
    const val TABLE_COLUMN_TIME = "recentSearchTime"
}