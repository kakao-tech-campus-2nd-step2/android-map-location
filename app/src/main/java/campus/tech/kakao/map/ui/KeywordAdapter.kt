package campus.tech.kakao.map.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R

class KeywordAdapter(private val listener: OnKeywordRemoveListener) : RecyclerView.Adapter<KeywordAdapter.ViewHolder>() {

    interface OnKeywordRemoveListener {
        fun onKeywordRemove(keyword: String)
        fun onKeywordClick(keyword: String)
    }

    private val keywords = mutableListOf<String>()

    val currentKeywords: List<String>
        get() = keywords

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_keyword, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keyword = keywords[position]
        holder.bind(keyword)
    }

    override fun getItemCount(): Int {
        return keywords.size
    }

    fun submitList(newKeywords: List<String>) {
        val oldSize = keywords.size
        keywords.clear()
        notifyItemRangeRemoved(0, oldSize)
        keywords.addAll(newKeywords)
        notifyItemRangeInserted(0, newKeywords.size)
    }

    fun addKeyword(keyword: String) {
        if (!keywords.contains(keyword)) {
            keywords.add(keyword)
            notifyItemInserted(keywords.size - 1)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvKeyword: TextView = itemView.findViewById(R.id.tvKeyword)
        private val ivRemove: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(keyword: String) {
            tvKeyword.text = keyword
            ivRemove.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    keywords.removeAt(position)
                    notifyItemRemoved(position)
                    listener.onKeywordRemove(keyword)
                }
            }
            itemView.setOnClickListener {
                listener.onKeywordClick(keyword)
            }
        }
    }
}
