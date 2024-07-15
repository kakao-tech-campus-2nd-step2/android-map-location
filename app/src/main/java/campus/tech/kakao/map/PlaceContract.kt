package campus.tech.kakao.map

import android.provider.BaseColumns

object PlaceContract: BaseColumns {
    const val TABLE_NAME = "place"
    const val TABLE_COLUMN_NAME = "name"
    const val TABLE_COLUMN_ADDRESS = "address"
    const val TABLE_COLUMN_CATEGORY = "category"
}