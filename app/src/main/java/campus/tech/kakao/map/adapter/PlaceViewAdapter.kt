package campus.tech.kakao.map.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.view.OnClickPlaceListener
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.Place

class PlaceViewAdapter(
    val listener: OnClickPlaceListener
) : ListAdapter<Place, PlaceViewHolder>(PlaceDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.place_item, parent, false)
        Log.d("testt", "검색 결과 뷰 생성")
        return PlaceViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
//        Log.d("testt", "${placeList?.get(position)?.name}, ${placeList?.get(position)?.location}, ${placeList?.get(position)?.category}")
        val currentPlace = getItem(position)
        holder.bind(currentPlace)
    }
}

class PlaceDiffCallBack : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}

class PlaceViewHolder(itemView: View, val listener: OnClickPlaceListener) :
    RecyclerView.ViewHolder(itemView) {
    val name = itemView.findViewById<TextView>(R.id.place_name)
    val location = itemView.findViewById<TextView>(R.id.place_location)
    val category = itemView.findViewById<TextView>(R.id.place_category)
    var currentPlace : Place? = null

    init {
        itemView.setOnClickListener {
            val position = absoluteAdapterPosition
            Log.d("testt", "콜백함수 호출")
            currentPlace?.let { listener.savePlace(it) }
        }
    }

    fun bind(place : Place){
        currentPlace = place
        name.text = place.name
        location.text = place.location ?: ""
        Log.d("testt", "입력값 : " + location.text.toString())
        category.text = place.category ?: ""
    }
}
