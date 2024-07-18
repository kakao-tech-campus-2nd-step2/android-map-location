package campus.tech.kakao.map
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemRecyclerviewBinding
class RecycleAdapter(private val onClick: (String) -> Unit) : RecyclerView.Adapter<RecycleAdapter.Holder>() {

    private var itemList = listOf<ListLayout>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun submitList(items: List<ListLayout>) {
        itemList = items
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListLayout) {
            binding.apply {
                Name.text = item.name
                Address.text = item.address
                Category.text = item.category

                itemView.setOnClickListener {
                    onClick(item.name)  // 클릭 시 콜백 함수 호출
                }
            }
        }
    }
}