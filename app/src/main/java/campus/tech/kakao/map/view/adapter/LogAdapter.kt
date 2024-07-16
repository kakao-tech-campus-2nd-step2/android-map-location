package campus.tech.kakao.map.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.util.DiffUtilCallback
import campus.tech.kakao.map.databinding.LogItemBinding
import campus.tech.kakao.map.domain.model.Place

class LogAdapter(
    private val onRemoveLog: (String) -> Unit
)
    : ListAdapter<Place, LogAdapter.LogViewHolder>(DiffUtilCallback()) {
    inner class LogViewHolder(private val binding: LogItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(place: Place){
            binding.place = place
            binding.btnLogDel.setOnClickListener {
                onRemoveLog(place.id)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = LogItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
    }
}