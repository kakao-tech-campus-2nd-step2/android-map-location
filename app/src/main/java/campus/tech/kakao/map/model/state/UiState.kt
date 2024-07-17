package campus.tech.kakao.map.model.state

sealed class UiState {
    object NotInitialized : UiState()

    object Success : UiState()

    object Loading : UiState()

    data class Error(val e: Exception) : UiState()
}
