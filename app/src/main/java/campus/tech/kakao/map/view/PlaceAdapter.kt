package campus.tech.kakao.map.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.viewmodel.MyViewModel
import campus.tech.kakao.map.model.data.Place
import campus.tech.kakao.map.R


class PlaceAdapter(private var items : List<Place>, private val onItemClick : (Place) -> Unit) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val onItemClick : (Place) -> Unit) : RecyclerView.ViewHolder(view){


        val id : TextView = view.findViewById(R.id.id)
        val name: TextView = view.findViewById(R.id.name)
        val address: TextView = view.findViewById(R.id.address)
        val kind: TextView = view.findViewById(R.id.kind)
        var longitude : Double? = null
        var latitude : Double? = null


        fun bind(item: Place) {
            itemView.setOnClickListener {
                it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)  //진동
                onItemClick(item)
//                viewModel.itemClick.value = item
            }
            id.text = item.id.toString()
            name.text = item.name
            address.text = item.address
            kind.text = item.kind
            longitude = item.longitude.toDouble()
            latitude = item.latitude.toDouble()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
        return ViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newItems: List<Place>) {
        items = newItems
        notifyDataSetChanged()
    }
}