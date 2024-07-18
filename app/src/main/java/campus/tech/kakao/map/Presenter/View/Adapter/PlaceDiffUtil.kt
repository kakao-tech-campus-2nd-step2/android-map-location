package campus.tech.kakao.map.Presenter.View.Adapter

import androidx.recyclerview.widget.DiffUtil
import campus.tech.kakao.map.Domain.Model.Place

class PlaceDiffUtil : DiffUtil.ItemCallback<Place>(){
    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.name == newItem.name
    }
}