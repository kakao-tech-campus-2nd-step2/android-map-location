package campus.tech.kakao.map.database
/*
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "location.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {

        val createCafeTable = """
            CREATE TABLE ${MapContract.TABLE_CAFE} (
                ${MapContract.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${MapContract.COLUMN_NAME} TEXT,
                ${MapContract.COLUMN_ADDRESS} TEXT,
                ${MapContract.COLUMN_CATEGORY} TEXT
            )
        """.trimIndent()

        val createPharmacyTable = """
            CREATE TABLE ${MapContract.TABLE_PHARMACY} (
                ${MapContract.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${MapContract.COLUMN_NAME} TEXT,
                ${MapContract.COLUMN_ADDRESS} TEXT,
                ${MapContract.COLUMN_CATEGORY} TEXT
            )
        """.trimIndent()

        db.execSQL(createCafeTable)
        db.execSQL(createPharmacyTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${MapContract.TABLE_CAFE}")
        db.execSQL("DROP TABLE IF EXISTS ${MapContract.TABLE_PHARMACY}")
        onCreate(db)
    }

    // step1 피드백 수정 부분
    fun insertData() {
        val db = writableDatabase

        for (i in 1..9) {
            val name = "카페$i"
            // 데이터가 있는지 체크 후 데이터 삽입 처리
            if (!dataExists(MapContract.TABLE_CAFE, name)) {
                val insertCafe = """
                    INSERT INTO ${MapContract.TABLE_CAFE} (${MapContract.COLUMN_NAME}, ${MapContract.COLUMN_ADDRESS}, ${MapContract.COLUMN_CATEGORY})
                    VALUES ('$name', '서울 성동구 성수동 $i', '카페')
                """.trimIndent()
                db.execSQL(insertCafe)
            }
        }

        for (i in 1..9) {
            val name = "약국$i"
            if (!dataExists(MapContract.TABLE_PHARMACY, name)) {
                val insertPharmacy = """
                    INSERT INTO ${MapContract.TABLE_PHARMACY} (${MapContract.COLUMN_NAME}, ${MapContract.COLUMN_ADDRESS}, ${MapContract.COLUMN_CATEGORY})
                    VALUES ('$name', '서울 강남구 대치동 $i', '약국')
                """.trimIndent()
                db.execSQL(insertPharmacy)
            }
        }

        db.close()
    }

    // 데이터 존재 여부 체킹 메소드
    private fun dataExists(tableName: String, name: String): Boolean {
        val db = readableDatabase
        val query = "SELECT 1 FROM $tableName WHERE ${MapContract.COLUMN_NAME} = ?"
        val cursor = db.rawQuery(query, arrayOf(name))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun searchPlaces(keyword: String): List<MapItem> {
        val db = readableDatabase
        val query = """
            SELECT ${MapContract.COLUMN_NAME}, ${MapContract.COLUMN_ADDRESS}, ${MapContract.COLUMN_CATEGORY}
            FROM ${MapContract.TABLE_CAFE}
            WHERE ${MapContract.COLUMN_NAME} LIKE ?
            UNION
            SELECT ${MapContract.COLUMN_NAME}, ${MapContract.COLUMN_ADDRESS}, ${MapContract.COLUMN_CATEGORY}
            FROM ${MapContract.TABLE_PHARMACY}
            WHERE ${MapContract.COLUMN_NAME} LIKE ?
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf("%$keyword%", "%$keyword%"))
        val results = mutableListOf<MapItem>()

        cursor.use {
            val nameIndex = cursor.getColumnIndex(MapContract.COLUMN_NAME)
            val addressIndex = cursor.getColumnIndex(MapContract.COLUMN_ADDRESS)
            val categoryIndex = cursor.getColumnIndex(MapContract.COLUMN_CATEGORY)

            if (nameIndex != -1 && addressIndex != -1 && categoryIndex != -1) {
                if (cursor.moveToFirst()) {
                    do {
                        val name = cursor.getString(nameIndex)
                        val address = cursor.getString(addressIndex)
                        val category = cursor.getString(categoryIndex)
                        results.add(MapItem(name, address, category))
                    } while (cursor.moveToNext())
                }
            }
        }

        return results
    }
}
*/
