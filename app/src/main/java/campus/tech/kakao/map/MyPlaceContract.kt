package campus.tech.kakao.map

import android.provider.BaseColumns

object MyPlaceContract {
    object Research : BaseColumns {
        const val TABLE_NAME = "research"
        const val COLUMN_NAME = "name"
        const val COLUMN_IMG = "img"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_CATEGORY = "category"
    }
}