package campus.tech.kakao.map.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.SearchResultItemBinding
import campus.tech.kakao.map.model.search.Place

class SearchResultsAdapter(
    private val searchResults: List<Place>,
    private val layoutInflater: LayoutInflater
) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    private lateinit var itemClickListener: OnItemClickListener

    inner class ViewHolder(private val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place) {
            binding.placeName.text = item.place_name
            binding.addressName.text = item.address_name
            binding.categoryName.text = item.category_name

            binding.root.setOnClickListener {
                itemClickListener?.onClick(item)
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
        holder.bind(searchResults[position])
    }

    interface OnItemClickListener {
        fun onClick(item: Place)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}