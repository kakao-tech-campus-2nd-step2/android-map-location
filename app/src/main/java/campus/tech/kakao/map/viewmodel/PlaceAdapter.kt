package campus.tech.kakao.map.viewmodel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemPlaceBinding
import campus.tech.kakao.map.model.Document
import campus.tech.kakao.map.view.MainActivity

class PlaceAdapter(
    private val context: Context,
    private var items: List<Document>,
    private val onItemClick: (Document) -> Unit
) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document, onItemClick: (Document) -> Unit) {
            binding.placeName.text = document.placeName
            binding.placeLocation.text = document.addressName
            binding.root.setOnClickListener {
                onItemClick(document)

                
                val sharedPreferences = context.getSharedPreferences("PlacePreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString(MainActivity.EXTRA_PLACE_LONGITUDE, document.x)
                editor.putString(MainActivity.EXTRA_PLACE_LATITUDE, document.y)
                editor.putString(MainActivity.EXTRA_PLACE_NAME, document.placeName)
                editor.putString(MainActivity.EXTRA_PLACE_ADDRESSNAME, document.addressName)
                editor.apply()


                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<Document>) {
        val diffResult = DiffUtil.calculateDiff(PlaceDiffCallback(items, newItems))
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    class PlaceDiffCallback(
        private val oldList: List<Document>,
        private val newList: List<Document>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
