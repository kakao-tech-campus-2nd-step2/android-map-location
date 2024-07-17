package campus.tech.kakao.map

interface SearchAdapterListener {
    fun displaySearchLocation(name: String, address: String, latitude: String, longitude: String)
}