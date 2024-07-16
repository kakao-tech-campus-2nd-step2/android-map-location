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
    val onClickDelete: (id : Int) -> Unit
) : ListAdapter<Place, FavoriteAdapter.ViewHolder>(PlaceDiffUtil()) {
    class ViewHolder(itemView: View, onClickDelete: (id : Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private var placeName: TextView
        private var deleteFavorite: ImageView
        private var curId : Int?

        init {
            placeName = itemView.findViewById<TextView>(R.id.favoriteName)
            deleteFavorite = itemView.findViewById<ImageView>(R.id.deleteFavorite)
            curId = null

            deleteFavorite.setOnClickListener {
                curId?.let {
                    onClickDelete.invoke(it)
                }
            }

        }


        fun bind(place: Place){
            placeName.text = place.name
            curId = place.id
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



