package campus.tech.kakao.map.domain.usecase

interface RemoveSearchQueryUseCase {
    operator fun invoke(query: String)
}