package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DatabaseManager(context: Context) {

    private val dbHelper = DatabaseHelper(context) //SQLiteOpenHelper객체 생성

    private val db: SQLiteDatabase = dbHelper.writableDatabase //open하고 실제 데이터베이스..?
    //예외 처리 try-catch로 처리하기

    fun insertPlace(name: String, address: String, kind: String) {
        val values = ContentValues().apply {
            put("name", name)
            put("address", address)
            put("kind", kind)
        }
        db.insert("places", null, values)
    }

//    fun searchPlacesKind(kind: String): List<Place> {   //kind로 검색하기
//        val result = mutableListOf<Place>()
//        val cursor: Cursor = db.query(
//            "places",   //검색할 테이블
//            arrayOf("id", "name", "address", "kind"),   //반환할 열의 배열
//            "kind = ?",  //필터링 where
//            arrayOf(kind),  //위에 ?에 들어갈 매개변수
//            null,
//            null,
//            null
//        )
//
//        with(cursor) {  //객체의 범위, 객체 이름을 지정하지 않아도됨!
//            while (moveToNext()) {  //moveToNext() 메서드는 다음 레코드로 이동하고, 이동할 수 있으면 true를 반환
//                val place = Place(
//                    getInt(getColumnIndexOrThrow("id")),
//                    getString(getColumnIndexOrThrow("name")),
//                    getString(getColumnIndexOrThrow("address")),
//                    getString(getColumnIndexOrThrow("kind"))
//                )
//                result.add(place)
//            }
//        }
//        cursor.close()
//        return result
//    }


    fun insertSavedsearch(id : Int, name: String) {
        val values = ContentValues().apply {
            put("id", id)
            put("name", name)
        }
        db.insert("SavedSearch", null, values)
    }

    fun getSavedSearches(): List<SavedSearch> { //savedSeach에서 검색해서 가져오기
        val result = mutableListOf<SavedSearch>()
        val cursor: Cursor = db.query(
            "SavedSearch",
            arrayOf("id", "name"),   //반환할 열의 배열
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val savedSearch = SavedSearch(
                    getInt(getColumnIndexOrThrow("id")),
                    getString(getColumnIndexOrThrow("name")),
                )
                result.add(savedSearch)
            }
        }
        cursor.close()
        return result
    }


    fun deleteSavedSearch(id: Int) {
        db.delete("SavedSearch", "id = ?", arrayOf(id.toString()))
    }

    fun dropTable(){
        dbHelper.dropTable(db)
    }

    fun createTable() {
        dbHelper.onCreate(db)
    }

}