package campus.tech.kakao.map

import android.provider.BaseColumns

object MapContract {
    object MapEntry {
        const val TABLE_NAME = "map"
        const val TABLE_NAME_HISTORY = "history"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_CATEGORY = "category"
        const val COLUMN_NAME_ADDRESS = "address"
        const val COLUMN_NAME_X = "x"
        const val COLUMN_NAME_Y = "y"
    }
}