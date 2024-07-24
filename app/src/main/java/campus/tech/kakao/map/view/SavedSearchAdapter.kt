package campus.tech.kakao.map.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.viewmodel.MyViewModel
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.data.SavedSearch


class SavedSearchAdapter(
    private var items: List<SavedSearch>,
    private val onCloseClick: (SavedSearch) -> Unit,
    private val onNameClick: (SavedSearch) -> Unit
) : RecyclerView.Adapter<SavedSearchAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        private val onCloseClick: (SavedSearch) -> Unit,
        private val onNameClick: (SavedSearch) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val close: ImageView = view.findViewById(R.id.close)
        private val id: TextView = view.findViewById(R.id.saved_search_id)
        private val name: TextView = view.findViewById(R.id.saved_search_name)

        fun bind(item: SavedSearch) {
            close.setOnClickListener {
                it.playSoundEffect(android.view.SoundEffectConstants.CLICK) //클릭 시 소리
                it.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY)  //가벼운 진동
                onCloseClick(item)
            }
            name.setOnClickListener {
                onNameClick(item)
            }
            id.text = item.id.toString()
            name.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_saved_search, parent, false)

        return ViewHolder(view, onCloseClick, onNameClick)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newItems: List<SavedSearch>) {
        items = newItems
        notifyDataSetChanged()
    }

}

