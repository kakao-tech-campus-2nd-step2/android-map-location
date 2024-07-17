package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemSearchResultBinding

class SearchResultsAdapter(
    private var keywords: List<Keyword>,
    private val onKeywordClick: (Keyword) -> Unit
) : RecyclerView.Adapter<SearchResultsAdapter.KeywordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeywordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(keywords[position])
    }

    override fun getItemCount(): Int = keywords.size

    inner class KeywordViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: Keyword) {
            binding.keyword = keyword
            binding.root.setOnClickListener { onKeywordClick(keyword) }
        }
    }

    fun updateData(newKeywords: List<Keyword>) {
        keywords = newKeywords
        notifyDataSetChanged()
    }
}