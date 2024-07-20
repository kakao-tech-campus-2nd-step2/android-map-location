package ksc.campus.tech.kakao.map

import ksc.campus.tech.kakao.map.models.repositories.MapViewRepository
import ksc.campus.tech.kakao.map.models.repositories.SearchKeywordRepository
import ksc.campus.tech.kakao.map.models.repositories.SearchResultRepository
import ksc.campus.tech.kakao.map.models.repositoriesImpl.FakeMapViewRepository
import ksc.campus.tech.kakao.map.models.repositoriesImpl.FakeSearchKeywordRepository
import ksc.campus.tech.kakao.map.models.repositoriesImpl.FakeSearchResultRepository
import ksc.campus.tech.kakao.map.models.repositoriesImpl.MapViewRepositoryImpl
import ksc.campus.tech.kakao.map.models.repositoriesImpl.SearchKeywordRepositoryImpl
import ksc.campus.tech.kakao.map.models.repositoriesImpl.SearchResultRepositoryImpl

class TestApplication: MyApplication() {
    override fun onCreate() {
        appContainer.addSingleton<SearchKeywordRepository>(FakeSearchKeywordRepository())
        appContainer.addSingleton<SearchResultRepository>(FakeSearchResultRepository())
        appContainer.addSingleton<MapViewRepository>(FakeMapViewRepository())
        super.onCreate()
    }
}