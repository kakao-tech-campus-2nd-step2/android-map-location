package campus.tech.kakao.map

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.adapter.AdapterCallback
import campus.tech.kakao.map.adapter.DocumentAdapter
import campus.tech.kakao.map.adapter.WordAdapter
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.SearchWord

class SearchActivity : AppCompatActivity(), AdapterCallback {

    private lateinit var model: MainViewModel
    private lateinit var search:EditText
    private lateinit var clear: TextView
    private lateinit var noResult: TextView
    private lateinit var searchResult: RecyclerView
    private lateinit var searchWordResult: RecyclerView
    private lateinit var documentAdapter: DocumentAdapter
    private lateinit var wordAdapter: WordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupUI()
        searchResult.layoutManager = LinearLayoutManager(this)
        searchWordResult.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        documentAdapter = DocumentAdapter(this)
        wordAdapter = WordAdapter(this)
        search.doOnTextChanged { text, _, _, _ ->
            val query = text.toString()
            if (query.isEmpty()){
                noResult.visibility = View.VISIBLE
                searchResult.visibility = View.GONE
            }else{
                noResult.visibility = View.GONE
                searchResult.visibility = View.VISIBLE
                model.searchLocalAPI(query)
            }
        }
        model = ViewModelProvider(this)[MainViewModel::class.java]
        model.documentList.observe(this, Observer {documents ->
            if (documents.isNullOrEmpty()){
                noResult.visibility = View.VISIBLE
                searchResult.visibility = View.GONE
            }else{
                noResult.visibility = View.GONE
                searchResult.visibility = View.VISIBLE
                documentAdapter.submitList(documents)
                searchResult.adapter = documentAdapter
            }
        })
        model.loadWord()
        model.wordList.observe(this, Observer {searchWords ->
            if (searchWords.isNullOrEmpty()){
                searchWordResult.visibility = View.GONE
            }
            else{
                searchWordResult.visibility = View.VISIBLE
                wordAdapter.submitList(searchWords)
                searchWordResult.adapter = wordAdapter
            }
        })
    }

    private fun setupUI(){
        search = findViewById(R.id.search)
        clear = findViewById(R.id.search_clear)
        noResult = findViewById(R.id.no_search_result)
        searchResult = findViewById(R.id.search_result_recycler_view)
        searchWordResult = findViewById(R.id.search_word_recycler_view)
        clear.setOnClickListener {
            search.setText("")
        }
    }

    override fun onWordAdded(document: Document) {
        model.addWord(document)
    }

    override fun onDocumentInfoSet(document: Document) {
        model.setMapInfo(document)
        finish()
    }

    override fun onWordDeleted(searchWord: SearchWord) {
        model.deleteWord(searchWord)
    }

    override fun onWordSearched(searchWord: SearchWord) {
        model.searchLocalAPI(searchWord.name)
    }


}
