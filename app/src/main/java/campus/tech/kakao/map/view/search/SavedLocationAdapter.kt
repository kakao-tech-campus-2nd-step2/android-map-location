package campus.tech.kakao.map.view.search

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.view.search.SavedLocationAdapter.SavedLocationHolder
import campus.tech.kakao.map.model.SavedLocation

class SavedLocationAdapter(
        private val itemSelectedListener: OnItemSelectedListener
) : ListAdapter<SavedLocation, SavedLocationHolder>(
    object: DiffUtil.ItemCallback<SavedLocation>() {
        override fun areItemsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
            return oldItem.title == newItem.title
        }
        override fun areContentsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
            return oldItem == newItem
        }
    }) {
    inner class SavedLocationHolder(
        itemView:View,
        itemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(itemView) {
        val savedLocationXButton: ImageView = itemView.findViewById(R.id.savedLocationXButton)
        val savedLocationTextView: TextView = itemView.findViewById(R.id.savedLocationTextView)

        init {
            savedLocationXButton.setOnClickListener {
                itemSelectedListener.deleteSavedLocation(getItem(bindingAdapterPosition) as SavedLocation)
            }
        }
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): SavedLocationHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_location, parent, false)
        return SavedLocationHolder(view, itemSelectedListener)
    }

    override fun onBindViewHolder(holder:SavedLocationHolder, position: Int) {
        val savedLocation = getItem(position)
        holder.savedLocationTextView.setText(savedLocation.title)
    }
}