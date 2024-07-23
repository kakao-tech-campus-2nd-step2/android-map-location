package campus.tech.kakao.map

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(
    private val context: Context,
    private var data: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<MyDatabaseHelper.SearchItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDatabaseHelper.SearchItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_place_item, parent, false)
        return MyDatabaseHelper.SearchItemViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: MyDatabaseHelper.SearchItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(newData: List<String>) {
        data = newData
        notifyDataSetChanged()
    }


}