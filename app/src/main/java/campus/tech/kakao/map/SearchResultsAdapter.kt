package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.SearchResultItemBinding


class SearchResultsAdapter(
    private val searchResults: List<Place>,
    private val layoutInflater: LayoutInflater,
    private val saveStoreName: (SearchKeyword) -> Unit
) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {
    inner class ViewHolder(binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val placeName: TextView = binding.placeName
        val addressName: TextView = binding.addressName
        val categoryName: TextView = binding.categoryName

        init {
            binding.root.setOnClickListener {
                val searchKeyword = SearchKeyword(placeName.text.toString())
                saveStoreName(searchKeyword)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchResultItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = searchResults[position]
        holder.placeName.text = item.place_name
        holder.addressName.text = item.address_name
        holder.categoryName.text = item.category_name
    }
}