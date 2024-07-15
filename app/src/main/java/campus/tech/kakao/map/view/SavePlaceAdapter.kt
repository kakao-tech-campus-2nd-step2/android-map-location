package campus.tech.kakao.map.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.SavePlace

class SavePlaceAdapter(
    private var savePlaces: List<SavePlace>,
    private val onItemClickListener: (SavePlace) -> Unit
) : RecyclerView.Adapter<SavePlaceAdapter.SavePlaceViewHolder>() {
    class SavePlaceViewHolder(
        itemView: View,
        private val onItemClickListener: (SavePlace) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val savePlaceTextView: TextView = itemView.findViewById(R.id.savePlace)
        private val savePlaceDeleteBtn: ImageView = itemView.findViewById(R.id.saveCancelBtn)

        fun bind(savePlace: SavePlace) {
            savePlaceTextView.text = savePlace.savePlace
            savePlaceDeleteBtn.setOnClickListener {
                onItemClickListener(savePlace)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavePlaceViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.saveplace_item, parent, false)
        return SavePlaceViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: SavePlaceViewHolder, position: Int) {
        val savePlace = savePlaces[position]
        holder.bind(savePlace)
    }

    override fun getItemCount(): Int {
        return savePlaces.size
    }

    fun updateData(newSavePlaces: List<SavePlace>) {
        savePlaces = newSavePlaces
        notifyDataSetChanged()
    }
}