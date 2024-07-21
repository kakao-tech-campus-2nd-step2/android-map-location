package campus.tech.kakao.map.presentation

import campus.tech.kakao.map.domain.model.Place

data class SearchUiState(
    val isloading: Boolean = false,
    val isError: Boolean = false,
    val Places: List<Place> = emptyList()

)
