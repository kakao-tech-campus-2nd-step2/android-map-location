package campus.tech.kakao.map.domain.model

import com.google.gson.annotations.SerializedName

data class PlaceMeta (
    @SerializedName("totalCount") var total_count: Int,
    @SerializedName("pageableCount") var pageable_count: Int,
    @SerializedName("isEnd") var is_end: Boolean
)