package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.SearchHistoryItemBinding

class HistoryAdapter(var items: List<SearchHistory>, val inflater: LayoutInflater, var itemClickListener: OnItemClickListener) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onXMarkClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = SearchHistoryItemBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(searchHistory: List<SearchHistory>) {
        items = searchHistory
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(private val binding: SearchHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(absoluteAdapterPosition)
            }

            binding.xmark.setOnClickListener {
                itemClickListener.onXMarkClick(absoluteAdapterPosition)
            }
        }
        fun bind(searchHistory: SearchHistory) {
            binding.history.text = searchHistory.searchHistory
        }
    }
}
