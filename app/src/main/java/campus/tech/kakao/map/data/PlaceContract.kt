package campus.tech.kakao.map.data

import android.provider.BaseColumns

class PlaceContract {
    object Place : BaseColumns {
        const val TABLE_NAME = "place"
        const val COLUMN_NAME = "name"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_X = "x"
        const val COLUMN_Y = "y"
    }
}
