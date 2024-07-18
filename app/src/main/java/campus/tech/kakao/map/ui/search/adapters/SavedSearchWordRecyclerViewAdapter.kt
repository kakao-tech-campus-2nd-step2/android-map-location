package campus.tech.kakao.map.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemSavedSearchWordBinding
import campus.tech.kakao.map.model.SavedSearchWord
import campus.tech.kakao.map.ui.search.SearchActivity

class SavedSearchWordRecyclerViewAdapter(
    private val savedSearchWordClearImageViewClickListener: SearchActivity.OnSavedSearchWordClearImageViewClickListener,
    private val savedSearchWordTextViewClickListener: SearchActivity.OnSavedSearchWordTextViewClickListener,
) :
    ListAdapter<SavedSearchWord, SavedSearchWordRecyclerViewAdapter.SavedSearchWordViewHolder>(
            SavedSearchWordDiffCallback(),
        ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SavedSearchWordViewHolder {
        val binding =
            ItemSavedSearchWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedSearchWordViewHolder(binding, savedSearchWordClearImageViewClickListener, savedSearchWordTextViewClickListener)
    }

    override fun onBindViewHolder(
        holder: SavedSearchWordViewHolder,
        position: Int,
    ) {
        val savedSearchWord = getItem(position)
        holder.bind(savedSearchWord)
    }

    class SavedSearchWordViewHolder(
        private val binding: ItemSavedSearchWordBinding,
        private val savedSearchWordImageViewClickListener: SearchActivity.OnSavedSearchWordClearImageViewClickListener,
        private val savedSearchWordTextViewClickListener: SearchActivity.OnSavedSearchWordTextViewClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(savedSearchWord: SavedSearchWord) {
            binding.savedSearchWordTextView.text = savedSearchWord.name
            binding.savedSearchWordTextView.setOnClickListener {
                savedSearchWordTextViewClickListener.onSavedSearchWordTextViewClicked(savedSearchWord)
            }
            binding.savedSearchWordClearImageView.setOnClickListener {
                savedSearchWordImageViewClickListener.onSavedSearchWordClearImageViewClicked(savedSearchWord)
            }
        }
    }

    private class SavedSearchWordDiffCallback : DiffUtil.ItemCallback<SavedSearchWord>() {
        override fun areItemsTheSame(
            oldItem: SavedSearchWord,
            newItem: SavedSearchWord,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SavedSearchWord,
            newItem: SavedSearchWord,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
