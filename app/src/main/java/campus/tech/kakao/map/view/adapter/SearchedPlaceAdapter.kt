package campus.tech.kakao.map.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.util.DiffUtilCallback
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.databinding.ListItemBinding


class SearchedPlaceAdapter(
    private var onItemClicked: (Place) -> Unit
): ListAdapter<Place, SearchedPlaceAdapter.LocationViewHolder>(DiffUtilCallback()) {

    inner class LocationViewHolder(private val binding: ListItemBinding )
        :RecyclerView.ViewHolder(binding.root){
            fun bind(place: Place){
                binding.place = place
                binding.root.setOnClickListener {
                    onItemClicked(place)
                }
            }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
    }
}