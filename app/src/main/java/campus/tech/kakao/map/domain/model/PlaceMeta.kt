package campus.tech.kakao.map.domain.model

import com.google.gson.annotations.SerializedName

data class PlaceMeta (
    @SerializedName("total_count") var totalCount: Int,
    @SerializedName("pageable_count") var pageableCount: Int,
    @SerializedName("is_end") var isEnd: Boolean
)