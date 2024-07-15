package campus.tech.kakao.map

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import campus.tech.kakao.map.databinding.ItemResultBinding
import android.util.Log

class SearchAdapter(private val onItemClicked: (Document) -> Unit) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val results = mutableListOf<Document>()

    class ViewHolder(val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        Log.d("SearchAdapter", "Binding result at position $position: $result")

        holder.binding.resultTextView.text = result.place_name
        holder.binding.resultAddressTextView.text = result.address_name
        holder.binding.resultCategoryTextView.text = result.category_group_name
        Log.e("SearchAdapter", "Unexpected result format: $result")

        holder.binding.root.setOnClickListener {
            onItemClicked(results[position])
        }
    }

    override fun getItemCount() = results.size

    fun updateResults(newResults: List<Document>) {
        Log.d("SearchAdapter", "Updating results: $newResults")
        results.clear()
        results.addAll(newResults)
        notifyDataSetChanged()
    }
}