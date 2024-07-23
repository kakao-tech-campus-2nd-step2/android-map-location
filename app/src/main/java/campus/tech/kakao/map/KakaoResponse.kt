package campus.tech.kakao.map

import com.google.gson.annotations.SerializedName

data class KakaoSearchResponse(
    @SerializedName("documents") val documents: List<Document>
)


data class RoadAddress(
    @SerializedName("building_name") val buildingName: String?
)
