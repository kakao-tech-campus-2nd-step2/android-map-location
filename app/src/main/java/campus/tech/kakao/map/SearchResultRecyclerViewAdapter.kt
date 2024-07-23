package campus.tech.kakao.map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.dto.Place

class ResultRecyclerViewAdapter(
    private var places: List<Place>,
    private val onItemClick: (Place) -> Unit
) : RecyclerView.Adapter<ResultRecyclerViewAdapter.PlaceViewHolder>() {

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(R.id.place_name)
        val placeCategory: TextView = itemView.findViewById(R.id.place_category)
        val placeAddress: TextView = itemView.findViewById(R.id.place_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.placeName.text = place.place_name
        holder.placeCategory.text = place.category_group_name
        holder.placeAddress.text = place.address_name
        holder.itemView.setOnClickListener { onItemClick(place) }
    }

    override fun getItemCount(): Int {
        return places.size
    }

    fun setPlaces(newPlaces: List<Place>) {
        places = newPlaces
        notifyDataSetChanged()
    }
}
