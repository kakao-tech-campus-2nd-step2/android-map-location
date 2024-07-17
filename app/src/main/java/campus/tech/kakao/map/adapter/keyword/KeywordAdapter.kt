package campus.tech.kakao.map.adapter.keyword

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.viewmodel.OnKeywordItemClickListener

class KeywordAdapter(private val onKeywordItemClickListener: OnKeywordItemClickListener) :
    ListAdapter<String, KeywordViewHolder>(
        object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String,
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String,
            ) = oldItem == newItem
        },
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): KeywordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_keyword, parent, false)
        return KeywordViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: KeywordViewHolder,
        position: Int,
    ) {
        holder.bindViewHolder(getItem(position), onKeywordItemClickListener)
    }
}

class KeywordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val remove: ImageView = view.findViewById(R.id.iv_remove_icon)
    private val keyword: TextView = view.findViewById(R.id.tv_keyword)

    fun bindViewHolder(
        keyword: String,
        onKeywordItemClickListener: OnKeywordItemClickListener,
    ) {
        this.keyword.text = keyword

        remove.setOnClickListener {
            onKeywordItemClickListener.onKeywordItemDeleteClick(keyword)
        }

        itemView.setOnClickListener {
            onKeywordItemClickListener.onKeywordItemClick(keyword)
        }
    }
}
