package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.jar.Attributes.Name

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "History.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE search_results (id INTEGER PRIMARY KEY, text TEXT)")
        db?.execSQL("CREATE TABLE saved_searches (id INTEGER PRIMARY KEY, text TEXT)")
        addDummySearchData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS search_results")
        db?.execSQL("DROP TABLE IF EXISTS saved_searches")
        onCreate(db)
    }

    private fun addDummySearchData(db: SQLiteDatabase?) {
        val dummySearchData = listOf(
            "Android", "Kotlin", "Kakao", "약국", "카페", "아무거나", "일단 테스트"
        )
        for (searchText in dummySearchData) {
            db?.execSQL("INSERT INTO saved_searches (text) VALUES (?)", arrayOf(searchText))
        }
    }

    fun insertSearchData(searchText: String) {
        val db = writableDatabase
        db.execSQL("INSERT INTO search_results (text) VALUES (?)", arrayOf(searchText))
        db.close()
    }

    fun deleteSearchData(searchText: String) {
        val db = writableDatabase
        db.execSQL("DELETE FROM search_results WHERE text = ?", arrayOf(searchText))
        db.close()
    }


    fun getSavedSearches(): List<String> {
        val db = readableDatabase
        val cursor = db.query("saved_searches", arrayOf("text"), null, null, null, null, null)
        val results = mutableListOf<String>()
        while (cursor.moveToNext()) {
            results.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return results
    }
    fun getSearchResults(searchText: String): List<String> {
        val db = readableDatabase
        val cursor = db.query(
            "search_results",
            arrayOf("text"),
            "text LIKE ?",
            arrayOf("%$searchText%"),
            null,
            null,
            null
        )
        val results = mutableListOf<String>()
        while (cursor.moveToNext()) {
            results.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return results
    }

    class SearchItemViewHolder(itemView: View, private val onItemClick: (String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val nameTextView:TextView = itemView.findViewById(R.id.name)
        private val addressTextView : TextView = itemView.findViewById(R.id.place)
        private val categoryTextView : TextView = itemView.findViewById((R.id.category))

        fun bind(searchText: String) {
            nameTextView.text = MapContract.COLUMN_NAME
            addressTextView.text = MapContract.COLUMN_ADDRESS
            categoryTextView.text = MapContract.COLUMN_CATEGORY
        }
    }
}




