package campus.tech.kakao.map.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.R

class LocationAdapter(
    private val locationList: List<LocationData>,
    private val onItemViewClick: (LocationData) -> Unit
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val locationTextView: TextView = itemView.findViewById(R.id.location)
        val categoryTextView: TextView = itemView.findViewById(R.id.category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = locationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locationList[position]
        holder.nameTextView.text = location.name
        holder.locationTextView.text = location.location
        holder.categoryTextView.text = location.category

        holder.itemView.setOnClickListener {
            onItemViewClick(location)
        }
    }
}