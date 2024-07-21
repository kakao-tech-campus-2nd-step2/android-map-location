package campus.tech.kakao.map.view

import campus.tech.kakao.map.domain.model.Place

data class UiState(
    val isloading: Boolean = false,
    val isError: Boolean = false,
    val Places: List<Place> = emptyList()

)
