package campus.tech.kakao.map.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.PlaceDataModel

class PlaceRecyclerViewAdapter(
    private val places: MutableList<PlaceDataModel>,
    private val onItemClick: (PlaceDataModel) -> Unit
) : RecyclerView.Adapter<PlaceRecyclerViewAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(R.id.tvPlaceName)
        val placeAddress: TextView = itemView.findViewById(R.id.tvPlaceAddress)
        val placeCategory: TextView = itemView.findViewById(R.id.tvPlaceCategory)

        init {
            itemView.setOnClickListener {
                val position: Int = bindingAdapterPosition
                val place = places[position]
                onItemClick(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.place_item, parent, false)
        return PlaceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
        holder.placeCategory.text = place.category
    }

    fun updateData(newPlace: List<PlaceDataModel>) {
        places.clear()
        places.addAll(newPlace)
    }
}
