package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SelectListAdapter(
    var selectItemList: List<KakaoMapItem>, val layoutInflater: LayoutInflater
) : RecyclerView.Adapter<SelectListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val cancelBtn: ImageView

        init {
            name = itemView.findViewById<TextView>(R.id.name)
            cancelBtn = itemView.findViewById<ImageView>(R.id.cancelBtn)

            cancelBtn.setOnClickListener {
                cancelBtnListener.onClick(it, selectItemList[bindingAdapterPosition])
            }
            itemView.setOnClickListener {
                itemListener.onClick(it, selectItemList[bindingAdapterPosition])
            }
        }
    }

    fun setCancelBtnClickListener(cancelBtnClickListener: ItemClickListener) {
        this.cancelBtnListener = cancelBtnClickListener
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemListener = itemClickListener
    }

    lateinit var cancelBtnListener: ItemClickListener
    lateinit var itemListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.select_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = selectItemList.get(position).name
    }

    override fun getItemCount(): Int {
        return selectItemList.size
    }

    fun updateMapItemList(mapItemList: List<KakaoMapItem>) {
        this.selectItemList = mapItemList
        notifyDataSetChanged()
    }
}