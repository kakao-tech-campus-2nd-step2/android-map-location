package campus.tech.kakao.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.AdapterCallback
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.MapActivity.Companion.documentClicked
import campus.tech.kakao.map.R

class DocumentAdapter(
	private val callback: AdapterCallback
): ListAdapter<Document, DocumentAdapter.ViewHolder>(
	object : DiffUtil.ItemCallback<Document>(){
		override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
			return oldItem === newItem
		}

		override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
			return oldItem == newItem
		}

	}
) {
	private fun placeClicked(position:Int){
		val document: Document = getItem(position)
		callback.onWordAdded(document)
		callback.onDocumentInfoSet(document)
		documentClicked = true
	}
	inner class ViewHolder(
		itemView: View
	): RecyclerView.ViewHolder(itemView) {
		val name:TextView = itemView.findViewById(R.id.name)
		val address:TextView = itemView.findViewById(R.id.address)
		val type:TextView = itemView.findViewById(R.id.type)
		init {
			itemView.setOnClickListener {
				placeClicked(bindingAdapterPosition)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val document: Document = getItem(position)
		holder.name.text = document.placeName
		holder.address.text = document.addressName
		holder.type.text = document.categoryGroupName
	}
}


