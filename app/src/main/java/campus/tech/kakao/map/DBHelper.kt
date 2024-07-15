package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context): SQLiteOpenHelper(context, "place.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${PlaceContract.TABLE_NAME} ("+
                    "${PlaceContract.TABLE_COLUMN_NAME} VARCHAR(30) NOT NULL,"+
                    "${PlaceContract.TABLE_COLUMN_ADDRESS} VARCHAR(50) NOT NULL,"+
                    "${PlaceContract.TABLE_COLUMN_CATEGORY} VARCHAR(30) NOT NULL"+
                    ");"
        )
        insertDummyData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${PlaceContract.TABLE_NAME}")
        onCreate(db)
    }

    fun insert(db: SQLiteDatabase, place: Place) {
        val sql = " INSERT INTO " +
                "${PlaceContract.TABLE_NAME}("+
                "${PlaceContract.TABLE_COLUMN_NAME}, ${PlaceContract.TABLE_COLUMN_ADDRESS}, ${PlaceContract.TABLE_COLUMN_CATEGORY})"+
                " VALUES(${place.name}, ${place.address}, ${place.category});"

        db.execSQL(sql)
    }

    fun select(db: SQLiteDatabase, name:String, address: String, category: String) : String? {
        return null
    }

    private fun insertDummyData(db: SQLiteDatabase?) {
        db?.let {
            val categories = listOf("카페", "약국", "식당")
            val baseAddress = "서울 성동구 성수동"

            categories.forEach { category ->
                for (i in 1..15) {
                    val name = "$category $i"
                    val address = "$baseAddress $i"
                    val sql = "INSERT INTO ${PlaceContract.TABLE_NAME} (" +
                            "${PlaceContract.TABLE_COLUMN_NAME}, ${PlaceContract.TABLE_COLUMN_ADDRESS}, ${PlaceContract.TABLE_COLUMN_CATEGORY}) " +
                            "VALUES ('$name', '$address', '$category');"
                    it.execSQL(sql)
                }
            }
        }
    }
}