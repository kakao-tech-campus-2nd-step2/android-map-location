package campus.tech.kakao.map.view.search

import campus.tech.kakao.map.model.SavedLocation

interface OnItemSelectedListener {
    fun onAddSavedLocation(title: String)
    fun onDeleteSavedLocation(item: SavedLocation)
    fun onUpdateLocationRecyclerView(title: String)
}