package campus.tech.kakao.map.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemPlaceSavedBinding

class SavedPlaceAdapter(
    private var items: List<String>,
    private val onItemRemove: (String) -> Unit
) : RecyclerView.Adapter<SavedPlaceAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemPlaceSavedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(query: String, onItemRemove: (String) -> Unit) {
            binding.queryText.text = query
            binding.removeButton.setOnClickListener { onItemRemove(query) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlaceSavedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onItemRemove)
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<String>) {
        val diffResult = DiffUtil.calculateDiff(SavedPlaceDiffCallback(items, newItems))
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    class SavedPlaceDiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
