package campus.tech.kakao.map.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import campus.tech.kakao.map.model.SavedSearchWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedSearchWordViewModel
    @Inject
    constructor(private val repository: SavedSearchWordRepository) :
    ViewModel() {
        private val _savedSearchWords = MutableStateFlow<List<SavedSearchWord>>(emptyList())
        val savedSearchWords: StateFlow<List<SavedSearchWord>> get() = _savedSearchWords

        init {
            getAllSearchWords()
        }

        fun insertSearchWord(searchWord: SavedSearchWord) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertOrUpdateSearchWord(searchWord)
                getAllSearchWords()
            }
        }

        fun deleteSearchWordById(searchWord: SavedSearchWord) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteSearchWordById(searchWord.id)
                val currentList = _savedSearchWords.value.toMutableList()
                currentList.remove(searchWord)
                _savedSearchWords.emit(currentList)
            }
        }

        private fun getAllSearchWords() {
            viewModelScope.launch(Dispatchers.IO) {
                val searchWords = repository.getAllSearchWords()
                _savedSearchWords.emit(searchWords)
            }
        }
    }
