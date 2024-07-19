package campus.tech.kakao.map.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.R

class MapViewAdapter(
    private var information: List<LocationData>,
) : RecyclerView.Adapter<MapViewAdapter.ViewHolder>() {

    fun updateData(newInformation: List<LocationData>) {
        information = newInformation
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.place_name)
        val locationTextView: TextView = itemView.findViewById(R.id.place_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_bottom_sheet, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = information.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = information[position]
        holder.nameTextView.text = info.name
        holder.locationTextView.text = info.location
    }


}