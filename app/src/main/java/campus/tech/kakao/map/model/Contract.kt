package campus.tech.kakao.map.model

import android.provider.BaseColumns

object Contract {
    object LocationEntry: BaseColumns{
        const val TABLE_NAME = "locations"
        const val COLUMN_NAME_TITLE = "name"
        const val COLUMN_NAME_ADDRESS = "address"
        const val COLUMN_NAME_CATEGORY = "category"
    }

    object SavedLocationEntry: BaseColumns{
        const val TABLE_NAME = "saved_locations"
        const val COLUMN_NAME_TITLE = "name"
    }
}