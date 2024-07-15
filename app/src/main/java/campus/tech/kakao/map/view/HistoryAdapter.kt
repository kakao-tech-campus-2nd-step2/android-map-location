package campus.tech.kakao.map.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemHistoryBinding
import campus.tech.kakao.map.viewmodel.SearchLocationViewModel

class HistoryAdapter(
    private var dataList: List<String>,
    private val context: Context,
    private val viewModel: SearchLocationViewModel
) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.removeLocationHistoryButton.setOnClickListener {
                viewModel.removeHistory(dataList[bindingAdapterPosition])
            }
        }

        fun binding(historyData: String) {
            binding.locationHistoryNameTextView.text = historyData
        }
    }

    fun updateDataList(newDataList: List<String>) {
        val diffUtil = HistoryDiffUtilCallback(dataList, newDataList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)

        dataList = newDataList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size
}