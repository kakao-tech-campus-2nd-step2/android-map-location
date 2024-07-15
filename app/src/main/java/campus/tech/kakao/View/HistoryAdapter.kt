package campus.tech.kakao.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R

class HistoryAdapter(private var historyList: MutableList<Pair<Int, String>>, private val itemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val (id, historyItem) = historyList[position]
        holder.bind(historyItem)

        holder.delButton.setOnClickListener {
            itemClickListener(id)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun updateData(newHistoryList: List<Pair<Int, String>>) {
        historyList.clear()
        historyList.addAll(newHistoryList)
        notifyDataSetChanged()
    }

    fun removeItemById(id: Int) {
        val index = historyList.indexOfFirst { it.first == id }
        if (index != -1) {
            historyList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val historyTextView: TextView = itemView.findViewById(R.id.historyTextView)
        internal val delButton: ImageButton = itemView.findViewById(R.id.delButton)

        fun bind(item: String) {
            historyTextView.text = item
        }
    }
}
