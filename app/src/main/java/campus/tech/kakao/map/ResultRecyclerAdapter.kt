package campus.tech.kakao.map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ResultRecyclerAdapter(
    var searchResult: List<Location>,
    val layoutInflater: LayoutInflater,
    val databaseListener: DatabaseListener
) : RecyclerView.Adapter<ResultRecyclerAdapter.MapViewHolder>() {
    inner class MapViewHolder(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.location_name)
        val category: TextView = itemView.findViewById(R.id.location_category)
        val address: TextView = itemView.findViewById(R.id.location_address)

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    databaseListener.insertHistory(name.text.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val view = layoutInflater.inflate(R.layout.item_search_result, parent, false)
        return MapViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        holder.name.text = searchResult[position].name
        holder.category.text = searchResult[position].category
        holder.address.text = searchResult[position].address
    }

    fun refreshList() {
        notifyDataSetChanged()
    }
}