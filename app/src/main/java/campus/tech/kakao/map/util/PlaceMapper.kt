package campus.tech.kakao.map.util

import campus.tech.kakao.map.domain.model.Place

class PlaceMapper {
    companion object{
        fun mapPlaces(places: List<Place>): List<Place> {
            return places.map { place ->
                place.copy(category = setCategoryName(place.category),
                    place = setPlaceName(place.place)
                )
            }
        }
        private fun setPlaceName(placeName: String): String {
            return if (placeName.length > 8){ placeName.take(8)+"..." } else placeName
        }
        private fun setCategoryName(categoryName: String): String {
            return categoryName.split(" ").lastOrNull() ?: ""
        }
    }
}