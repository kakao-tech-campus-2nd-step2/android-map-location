package campus.tech.kakao.map.Presenter.View.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.R
import campus.tech.kakao.map.Domain.Model.PlaceCategory

class SearchResultAdapter(
    val onClickAdd: (name: String) -> Unit
) : ListAdapter<Place, SearchResultAdapter.ViewHolder>(PlaceDiffUtil()) {

    class ViewHolder(itemView: View, onClickAdd: (name: String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private var placeName: TextView
        private var placeAddress: TextView
        private var placeCategory: TextView

        init {
            placeName = itemView.findViewById<TextView>(R.id.placeName)
            placeAddress = itemView.findViewById<TextView>(R.id.placeAddress)
            placeCategory = itemView.findViewById<TextView>(R.id.placeCategory)

            itemView.setOnClickListener {
                onClickAdd.invoke(placeName.text.toString())
            }
        }

        fun bind(place:Place){
            placeName.text = place.name
            placeAddress.text = place.address ?: ""
            placeCategory.text =
                PlaceCategory.categoryToString(place.category ?: PlaceCategory.ELSE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_element, parent, false)

        return ViewHolder(view, onClickAdd)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}