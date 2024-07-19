package campus.tech.kakao.map.dto

import campus.tech.kakao.map.BuildConfig

object UrlContract {
	const val BASE_URL = "https://dapi.kakao.com/v2/local/search/"
	const val AUTHORIZATION = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
}