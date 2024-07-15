package campus.tech.kakao.map.utils

import campus.tech.kakao.map.BuildConfig


object ApiKeyProvider {
    val KAKAO_REST_API_KEY = "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY
    val KAKAO_API_KEY = BuildConfig.KAKAO_API_KEY
}