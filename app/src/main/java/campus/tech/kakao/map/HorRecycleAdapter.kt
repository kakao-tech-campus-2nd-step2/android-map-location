package campus.tech.kakao.map

import android.database.Cursor
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.HoritemRecyclerviewBinding


class HorRecycleAdapter(private val onClick : (String, String, String, String) -> Unit, private val onDeleteClick: (String) -> Unit ) : RecyclerView.Adapter<HorRecycleAdapter.Holder>() {


    private var cursor:Cursor? = null

    inner class Holder(private val binding: HoritemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String, x: String, y: String, address: String) {
            binding.Name.text = name

            // 전체 항목(root)을 클릭했을 때의 동작
            binding.root.setOnClickListener {
                onClick(name, x, y,address)
            }

            // Delete 버튼을 클릭했을 때의 동작
            binding.Delete.setOnClickListener {
                onDeleteClick(name)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorRecycleAdapter.Holder {
        val binding = HoritemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    override fun onBindViewHolder(holder: HorRecycleAdapter.Holder, position: Int) {
        cursor?.apply {
            moveToPosition(position)
            val name = getString(getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME))
            val x = getString(getColumnIndexOrThrow(HistoryEntry.COLUMN_X))
            val y = getString(getColumnIndexOrThrow(HistoryEntry.COLUMN_Y))
            val address = getString(getColumnIndexOrThrow(HistoryEntry.COLUMN_ADDRESS))
            holder.bind(name, x, y,address)
        }
    }

    fun SubmitCursor(Cursor : Cursor?){
        cursor = Cursor
        notifyDataSetChanged()

    }
}
