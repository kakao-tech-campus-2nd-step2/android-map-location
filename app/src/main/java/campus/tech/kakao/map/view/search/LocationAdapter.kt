package campus.tech.kakao.map.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.view.search.LocationAdapter.LocationHolder
import campus.tech.kakao.map.model.Location

class LocationAdapter(
    private val itemSelectedListener: OnItemSelectedListener
) : ListAdapter<Location, LocationHolder>(
    object : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.title == newItem.title
        }
        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }) {
    inner class LocationHolder(
        itemView: View,
        itemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView by lazy { itemView.findViewById(R.id.titleTextView) }
        val addressTextView: TextView by lazy { itemView.findViewById(R.id.addressTextView) }
        val categoryTextView: TextView by lazy { itemView.findViewById(R.id.categoryTextView) }

        init {
            itemView.setOnClickListener {
                itemSelectedListener.addSavedLocation(getItem(bindingAdapterPosition).title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return LocationHolder(view, itemSelectedListener)
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        val location = getItem(position)
        holder.titleTextView.text = location.title
        holder.addressTextView.text = location.address
        holder.categoryTextView.text = location.category
    }
}