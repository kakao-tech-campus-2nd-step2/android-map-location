package campus.tech.kakao.map.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchResults(
    @SerializedName("documents")
    val places: List<Place>
)

@Parcelize
data class Place(
    val place_name: String,
    val category_name: String,
    val address_name: String,
    val x: String,
    val y: String
): Parcelable {
    fun getLng():Double = x.toDoubleOrNull() ?: 0.0
    fun getLat():Double = y.toDoubleOrNull() ?: 0.0
}