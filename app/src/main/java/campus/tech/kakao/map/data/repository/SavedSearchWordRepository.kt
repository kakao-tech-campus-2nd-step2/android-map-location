package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.model.SavedSearchWord

interface SavedSearchWordRepository {
    fun insertOrUpdateSearchWord(searchWord: SavedSearchWord)

    fun getAllSearchWords(): List<SavedSearchWord>

    fun deleteSearchWordById(id: Long)
}
