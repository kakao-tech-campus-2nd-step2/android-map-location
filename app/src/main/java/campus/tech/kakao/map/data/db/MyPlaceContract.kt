package campus.tech.kakao.map.data.db

import android.provider.BaseColumns

object MyPlaceContract {
    object Research : BaseColumns {
        const val TABLE_NAME = "research"
        const val COLUMN_NAME = "name"
        const val COLUMN_IMG = "img"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_X = "x"
        const val COLUMN_Y = "y"
    }
}