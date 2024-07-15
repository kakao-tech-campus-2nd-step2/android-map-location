package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context

class SavedSearchKeywordRepository(context: Context) {

    private val dbHelper = StoreInfoDBHelper(context)

    fun saveSearchKeyword(searchKeyword: SearchKeyword) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SearchKeywordEntry.SEARCH_KEYWORD, searchKeyword.searchKeyword)
        }
        db.insert(SearchKeywordEntry.TABLE_NAME, null, values)
        db.close()
    }

    fun getSavedSearchKeywords(): List<SearchKeyword> {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            SearchKeywordEntry.SEARCH_KEYWORD
        )

        val cursor = db.query(
            SearchKeywordEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val savedSearchKeywords = mutableListOf<SearchKeyword>()
        with(cursor) {
            while (moveToNext()) {
                val searchWord = getString(getColumnIndexOrThrow(SearchKeywordEntry.SEARCH_KEYWORD))

                val savedSearchWord = SearchKeyword(searchWord)
                savedSearchKeywords.add(savedSearchWord)
            }
        }
        cursor.close()
        db.close()
        return savedSearchKeywords
    }

    fun delSavedSearchKeyword(searchKeyword: SearchKeyword) {
        val db = dbHelper.writableDatabase
        val selection = "${SearchKeywordEntry.SEARCH_KEYWORD} = ?"
        val selectionArgs = arrayOf(searchKeyword.searchKeyword)
        db.delete(SearchKeywordEntry.TABLE_NAME, selection, selectionArgs)
        db.close()
    }
}