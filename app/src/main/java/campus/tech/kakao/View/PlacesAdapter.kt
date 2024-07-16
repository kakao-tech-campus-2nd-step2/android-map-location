package campus.tech.kakao.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.Model.Place
import campus.tech.kakao.map.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesAdapter(private var places: List<Place>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        val categoryGroupCode = CategoryGroupCode()

        val x = place.x
        val y = place.y
        val placeName = place.placeName
        val roadAddressName = place.roadAddressName
        holder.nameTextView.text = placeName
        holder.addressTextView.text = roadAddressName
        holder.categoryTextView.text = categoryGroupCode.CodeToCategory[place.categoryName] ?: ""
        holder.itemView.setOnClickListener {
            onItemClick(placeName)

            (holder.itemView.context as? MainActivity)?.lifecycleScope?.launch {
                navigateToMapFragment(x, y, placeName, roadAddressName!!, holder.itemView.context as MainActivity)
            }
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }

    fun updateData(newPlaces: List<Place>) {
        places = newPlaces
        notifyDataSetChanged()
    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
    }

    private suspend fun navigateToMapFragment(x: Double?, y: Double?, placeName: String, roadAddressName: String, activity: MainActivity) {
        withContext(Dispatchers.Main) {activity.clearBackStack()
            val fragment = MapFragment().apply {
                arguments = Bundle().apply {
                    putDouble("x", x!!)
                    putDouble("y", y!!)
                    putString("placeName", placeName)
                    putString("roadAddressName", roadAddressName)
                }
            }

            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()

            activity.supportFragmentManager.executePendingTransactions()

            val mapFragment = activity.supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? MapFragment
            mapFragment?.setCoordinates(x!!, y!!, placeName, roadAddressName)
        }
    }}