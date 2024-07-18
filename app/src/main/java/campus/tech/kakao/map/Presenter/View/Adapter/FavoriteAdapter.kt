package campus.tech.kakao.map.Presenter.View.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.R

class FavoriteAdapter(
    val onClickDelete: (name: String) -> Unit
) : ListAdapter<Place, FavoriteAdapter.ViewHolder>(PlaceDiffUtil()) {
    class ViewHolder(itemView: View, onClickDelete: (name: String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private var placeName: TextView
        private var deleteFavorite: ImageView

        init {
            placeName = itemView.findViewById<TextView>(R.id.favoriteName)
            deleteFavorite = itemView.findViewById<ImageView>(R.id.deleteFavorite)

            deleteFavorite.setOnClickListener {
                onClickDelete.invoke(placeName.text.toString())
            }
        }

        fun bind(place: Place){
            placeName.text = place.name
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_element, parent, false)
        return ViewHolder(view, onClickDelete)
    }

}



