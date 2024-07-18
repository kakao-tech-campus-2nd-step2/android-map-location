package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    private val onItemClicked: (Place) -> Unit
) : ListAdapter<Place, RecyclerViewAdapter.ViewHolder>(
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
        private val img: ImageView = itemView.findViewById(R.id.place_img)
        private val name: TextView = itemView.findViewById(R.id.place_name)
        private val location: TextView = itemView.findViewById(R.id.place_location)
        private val category: TextView = itemView.findViewById(R.id.place_category)

        fun bind(place: Place) {
            img.setImageResource(place.category.imgId)
            name.text = place.name
            location.text = place.location
            category.text = place.category.category

            itemView.setOnClickListener { onItemClicked(place) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.place_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place: Place = getItem(position)
        holder.bind(place)
    }
}