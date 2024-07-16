package campus.tech.kakao.map.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.SavedSearchKeywordItemBinding
import campus.tech.kakao.map.model.SearchKeyword

class SavedSearchKeywordsAdapter(
    private val savedSearchKeywords: List<SearchKeyword>,
    private val layoutInflater: LayoutInflater,
    private val getSearchResults: (SearchKeyword) -> Unit,
    private val delSavedSearchKeyword: (SearchKeyword) -> Unit
) :
    RecyclerView.Adapter<SavedSearchKeywordsAdapter.ViewHolder>() {
    inner class ViewHolder(binding: SavedSearchKeywordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val savedSearchKeyword: TextView = binding.SavedSearchKeyword

        init {
            binding.SavedSearchKeyword.setOnClickListener {
                val searchKeyword = SearchKeyword(savedSearchKeyword.text.toString())
                getSearchResults(searchKeyword)
            }
            binding.delSavedSearchKeyword.setOnClickListener {
                val searchKeyword = SearchKeyword(savedSearchKeyword.text.toString())
                delSavedSearchKeyword(searchKeyword)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SavedSearchKeywordItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return savedSearchKeywords.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = savedSearchKeywords[position]
        holder.savedSearchKeyword.text = item.searchKeyword
    }
}