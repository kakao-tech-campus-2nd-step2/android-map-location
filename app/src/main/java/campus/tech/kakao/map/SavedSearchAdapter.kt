package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemSavedSearchBinding

class SavedSearchAdapter(private val onSearchClicked: (String) -> Unit, private val onDeleteClicked: (String) -> Unit) : RecyclerView.Adapter<SavedSearchAdapter.ViewHolder>() {
    private val searches = mutableListOf<String>()

    class ViewHolder(val binding: ItemSavedSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavedSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val search = searches[position]
        holder.binding.savedTextView.text = search
        holder.binding.savedTextView.setOnClickListener {
            onSearchClicked(search)
        }
        holder.binding.deleteButton.setOnClickListener {
            onDeleteClicked(search)
        }
    }

    override fun getItemCount(): Int {
        return searches.size
    }

    fun updateSearches(newSearches: List<String>) {
        searches.clear()
        searches.addAll(newSearches)
        notifyDataSetChanged()
    }
}
