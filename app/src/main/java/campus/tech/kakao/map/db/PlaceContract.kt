package campus.tech.kakao.map.db

import android.provider.BaseColumns

object PlaceContract{
    const val DATABASE_NAME = "PLACE.DB"
    const val VERSION = 2

    object PlaceEntry : BaseColumns {
        const val TABLE_NAME = "PLACE"
        const val COLUMN_NAME = "NAME"
        const val COLUMN_LOCATION = "LOCATION"
        const val COLUMN_CATEGORY = "CATEGORY"
    }

    object SavedPlaceEntry : BaseColumns{
        const val TABLE_NAME = "SAVED_PLACE"
        const val COLUMN_NAME = "SAVED_NAME"
    }
}