package campus.tech.kakao.map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class HistoryRecyclerAdapter(
    var history: List<String>,
    val layoutInflater: LayoutInflater,
    val databaseListener: DatabaseListener
) : RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder>() {
    inner class HistoryViewHolder(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.history_name)
        val clear: ImageButton = itemView.findViewById(R.id.history_clear)

        init {
            clear.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    databaseListener.deleteHistory(name.text.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = layoutInflater.inflate(R.layout.item_search_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.name.text = history[position]
    }

    override fun getItemCount(): Int {
        return history.size
    }

    fun refreshList() {
        notifyDataSetChanged()
    }
}