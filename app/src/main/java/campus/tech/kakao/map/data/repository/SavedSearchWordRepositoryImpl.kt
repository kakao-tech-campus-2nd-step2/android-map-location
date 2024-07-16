package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.data.SavedSearchWordDBHelper
import campus.tech.kakao.map.model.SavedSearchWord
import javax.inject.Inject

class SavedSearchWordRepositoryImpl
    @Inject
    constructor(private val dbHelper: SavedSearchWordDBHelper) :
    SavedSearchWordRepository {
        override fun insertOrUpdateSearchWord(searchWord: SavedSearchWord) {
            dbHelper.insertOrUpdateSearchWord(searchWord)
        }

        override fun getAllSearchWords(): List<SavedSearchWord> {
            return dbHelper.getAllSearchWords()
        }

        override fun deleteSearchWordById(id: Long) {
            dbHelper.deleteSearchWordById(id)
        }
    }
