package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.dto.Place

class SearchHistoryRecyclerViewAdapter(
    private val searchHistory: MutableList<Place>,
    private val onItemClick: (Int) -> Unit,
    private val onItemDelete: (Int) -> Unit
) : RecyclerView.Adapter<SearchHistoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = searchHistory[position]
        holder.bind(place, position)
    }

    override fun getItemCount(): Int {
        return searchHistory.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val searchHistoryBtn: TextView = itemView.findViewById(R.id.search_history_item)
        private val searchHistoryDeleteBtn: ImageView = itemView.findViewById(R.id.search_history_delete_button)

        fun bind(place: Place, position: Int) {
            searchHistoryBtn.text = place.place_name
            searchHistoryBtn.setOnClickListener { onItemClick(position) }
            searchHistoryDeleteBtn.setOnClickListener {
                onItemDelete(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, searchHistory.size)
            }
        }
    }
}
