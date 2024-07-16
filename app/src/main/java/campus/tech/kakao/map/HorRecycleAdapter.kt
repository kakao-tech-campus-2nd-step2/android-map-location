package campus.tech.kakao.map

import android.database.Cursor
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.HoritemRecyclerviewBinding


class HorRecycleAdapter(private val onClick : (String) -> Unit ) : RecyclerView.Adapter<HorRecycleAdapter.Holder>() {

    private var cursor:Cursor? = null

    inner class Holder(private val binding: HoritemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String) {
            binding.Name.text = name
            binding.root.setOnClickListener{
                onClick(name)
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
            holder.bind(name)
        }
    }

    fun SubmitCursor(Cursor : Cursor?){
        cursor = Cursor
        notifyDataSetChanged()

    }
}
