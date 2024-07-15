package campus.tech.kakao.map.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.PlaceItemBinding
import campus.tech.kakao.map.domain.model.PlaceVO

class PlaceAdapter(
    private val onItemClick: (PlaceVO) -> Unit,
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {
    private var places: List<PlaceVO> = emptyList()

    class PlaceViewHolder(private val binding: PlaceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceVO, onItemClick: (PlaceVO) -> Unit) {
            binding.nameTextView.text = place.placeName
            binding.addressTextView.text = place.addressName
            binding.typeTextView.text = place.categoryName
            binding.root.setOnClickListener {
                onItemClick(place)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaceViewHolder {
        val binding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(
        holder: PlaceViewHolder,
        position: Int,
    ) {
        holder.bind(places[position], onItemClick)
    }

    fun updateData(newPlaces: List<PlaceVO>) {
        places = newPlaces
        notifyDataSetChanged()
    }
}
