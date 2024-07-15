package campus.tech.kakao.map

import android.provider.BaseColumns

object MapContract {
    object MapEntry : BaseColumns {
        const val TABLE_NAME = "map"
        const val TABLE_NAME_HISTORY = "history"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_CATEGORY = "category"
        const val COLUMN_NAME_ADDRESS = "address"
    }
}