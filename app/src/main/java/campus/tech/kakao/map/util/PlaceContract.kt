package campus.tech.kakao.map.util

object PlaceContract {
    const val DATABASE_NAME = "place.db"

    const val TABLE_NAME: String = "db_place"
    const val COLUMN_ID: String = "id"
    const val COLUMN_NAME: String = "name"
    const val COLUMN_LOCATION: String = "place"
    const val COLUMN_TYPE: String = "type"

    const val CREATE_QUERY = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ID TEXT NOT NULL, " +
            "$COLUMN_NAME TEXT NOT NULL, " +
            "$COLUMN_LOCATION TEXT NOT NULL, " +
            "$COLUMN_TYPE TEXT" +
            ");"

    const val DELETE_QUERY = "DELETE FROM $TABLE_NAME"
    const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"

    const val TABLE_LOG_NAME = "db_Log"
    const val COLUMN_LOG_ID = "log_id"
    const val COLUMN_LOG_NAME = "log_name"

    const val CREATE_LOG_QUERY = "CREATE TABLE IF NOT EXISTS $TABLE_LOG_NAME (" +
            "$COLUMN_LOG_ID TEXT NOT NULL, " +
            "$COLUMN_LOG_NAME TEXT NOT NULL" +
            ");"

    const val DELETE_LOG_QUERY = "DELETE FROM $TABLE_LOG_NAME"
    const val DROP_LOG_QUERY = "DROP TABLE IF EXISTS $TABLE_LOG_NAME"
}
