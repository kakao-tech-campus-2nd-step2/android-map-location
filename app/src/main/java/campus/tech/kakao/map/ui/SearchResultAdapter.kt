package campus.tech.kakao.map.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.viewmodel.MapItem
import campus.tech.kakao.map.R

class SearchResultAdapter(private val listener: OnItemClickListener) : ListAdapter<MapItem, SearchResultAdapter.ViewHolder>(
    DiffCallback()
) {

    interface OnItemClickListener {
        fun onItemClick(item: MapItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPlaceName: TextView = itemView.findViewById(R.id.tvPlaceName)
        private val tvPlaceAddress: TextView = itemView.findViewById(R.id.tvPlaceAddress)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }

        fun bind(item: MapItem) {
            tvPlaceName.text = item.name
            tvPlaceAddress.text = item.address
            tvCategory.text = item.category
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MapItem>() {
        override fun areItemsTheSame(oldItem: MapItem, newItem: MapItem): Boolean {
            return oldItem.name == newItem.name && oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: MapItem, newItem: MapItem): Boolean {
            return oldItem == newItem
        }
    }
}
