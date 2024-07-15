package campus.tech.kakao.map.model

import android.provider.BaseColumns

object PlaceContract {
    object SavePlaceEntry : BaseColumns {
        const val TABLE_NAME = "savePlace"
        const val COLUMN_PLACE_NAME = "savePlaceName"
        private const val COLUMN_TIMESTAMP = "timestamp"
        const val CREATE_QUERY = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "$COLUMN_PLACE_NAME varchar(30)," +
                "$COLUMN_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)"
        const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}