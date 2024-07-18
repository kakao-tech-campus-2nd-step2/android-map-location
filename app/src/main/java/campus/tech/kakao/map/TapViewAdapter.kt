package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TapViewAdapter(
    private val onItemRemoved: (Place) -> Unit
) : ListAdapter<Place, TapViewAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Place>(){
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }

    }
) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cancelButton: ImageView = itemView.findViewById(R.id.tab_close_button)
        private val placeName: TextView = itemView.findViewById(R.id.tab_place_textview)
        fun bind(place: Place) {
            placeName.text = place.name
            cancelButton.setOnClickListener {
                onItemRemoved(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.tab_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place: Place = getItem(position)
        holder.bind(place)
    }
}