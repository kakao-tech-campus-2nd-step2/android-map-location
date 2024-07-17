package campus.tech.kakao.map.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.view.OnClickSavedPlaceListener
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace

class SavedPlaceViewAdapter(
    val listener: OnClickSavedPlaceListener
) : ListAdapter<SavedPlace, SavedPlaceViewHolder>(SavedPlaceDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedPlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.saved_place_item, parent, false)
        Log.d("testt", "저장된 장소를 띄우는 뷰 홀더 생성")
        return SavedPlaceViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: SavedPlaceViewHolder, position: Int) {
        val currentSavedPlace = getItem(position)
        holder.bind(currentSavedPlace)
    }
}

class SavedPlaceDiffCallBack : DiffUtil.ItemCallback<SavedPlace>() {
    override fun areItemsTheSame(oldItem: SavedPlace, newItem: SavedPlace): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SavedPlace, newItem: SavedPlace): Boolean {
        return oldItem.name == newItem.name
    }
}

class SavedPlaceViewHolder(itemView: View, val listener: OnClickSavedPlaceListener) :
    RecyclerView.ViewHolder(itemView) {
    val name = itemView.findViewById<TextView>(R.id.saved_place_name)
    val deleteButton = itemView.findViewById<ImageView>(R.id.button_saved_delete)
    var currentSavedPlace: SavedPlace? = null

    init {
        deleteButton.setOnClickListener {
            val position = absoluteAdapterPosition
            Log.d("testt", "삭제 콜백함수 호출")
            currentSavedPlace?.let { listener.deleteSavedPlace(it, position) }
        }
    }

    fun bind(savedPlace: SavedPlace) {
        currentSavedPlace = savedPlace
        name.text = savedPlace.name
    }

}