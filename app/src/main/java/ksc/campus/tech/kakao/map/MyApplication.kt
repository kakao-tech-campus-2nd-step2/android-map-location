package ksc.campus.tech.kakao.map

import android.app.Application
import ksc.campus.tech.kakao.map.models.repositories.MapViewRepository
import ksc.campus.tech.kakao.map.models.repositories.SearchKeywordRepository
import ksc.campus.tech.kakao.map.models.repositories.SearchResultRepository
import ksc.campus.tech.kakao.map.models.repositoriesImpl.MapViewRepositoryImpl
import ksc.campus.tech.kakao.map.models.repositoriesImpl.SearchKeywordRepositoryImpl
import ksc.campus.tech.kakao.map.models.repositoriesImpl.SearchResultRepositoryImpl

open class MyApplication: Application() {
    val appContainer:AppContainer = AppContainer()
}