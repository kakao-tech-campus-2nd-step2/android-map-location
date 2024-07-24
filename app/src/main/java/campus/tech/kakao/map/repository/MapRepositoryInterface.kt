package campus.tech.kakao.map.repository

interface MapRepositoryInterface {
    fun getLastLocation(): Pair<Double, Double>?
}