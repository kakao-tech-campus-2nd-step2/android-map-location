package ksc.campus.tech.kakao.map.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.models.SearchResult


class SearchResultAdapter(
    val onItemClicked: ((item: SearchResult, index: Int) -> Unit)
) :
    ListAdapter<SearchResult, SearchResultAdapter.SearchResultViewHolder>(object :
        DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
            oldItem == newItem

    }) {
    class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var holdingData: SearchResult = SearchResult("-1", "", "", "", 0.0, 0.0)
            private set
        private val resultName: TextView
        private val resultAddress: TextView
        private val resultType: TextView

        init {
            resultName = itemView.findViewById(R.id.text_result_name)
            resultAddress = itemView.findViewById(R.id.text_result_address)
            resultType = itemView.findViewById(R.id.text_result_type)
        }

        fun bindData(item: SearchResult){
            holdingData = item
            resultName.text = item.name
            resultAddress.text = item.address
            resultType.text = item.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        val holder = SearchResultViewHolder(view)
        view.setOnClickListener {
            onItemClicked(
                holder.holdingData,
                holder.bindingAdapterPosition
            )
        }
        return holder
    }


    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = currentList[position]
        holder.bindData(item)
    }
}