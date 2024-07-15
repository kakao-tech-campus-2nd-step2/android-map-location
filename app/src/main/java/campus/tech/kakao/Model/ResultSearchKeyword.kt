package campus.tech.kakao.Model

import com.google.gson.annotations.SerializedName

data class ResultSearchKeyword(
    @SerializedName("documents") val documents: List<Place>
)