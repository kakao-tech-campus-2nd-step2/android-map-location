package campus.tech.kakao.map.util

import androidx.recyclerview.widget.DiffUtil
import campus.tech.kakao.map.domain.model.Place

class DiffUtilCallback: DiffUtil.ItemCallback<Place>(){
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}