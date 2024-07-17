package campus.tech.kakao.map.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.R

class SearchViewAdapter(
    private val searchList: ArrayList<LocationData>,
    private val onRemoveClick: (LocationData) -> Unit // 콜백 함수
) : RecyclerView.Adapter<SearchViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cancelBtn: Button = view.findViewById(R.id.searchViewCancelBtn)
        val nameText: TextView = view.findViewById(R.id.searchViewName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = searchList[position]
        holder.nameText.text = item.name
        holder.cancelBtn.setOnClickListener {
            onRemoveClick(item)
        }
    }

    override fun getItemCount() = searchList.size
}