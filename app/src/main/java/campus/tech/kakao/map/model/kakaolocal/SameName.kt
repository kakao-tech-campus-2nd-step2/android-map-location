package campus.tech.kakao.map.model.kakaolocal

import com.google.gson.annotations.SerializedName

data class SameName(
    val keyword: String,
    val region: List<String>,
    @SerializedName("selected_region") val selectedRegion: String,
)
