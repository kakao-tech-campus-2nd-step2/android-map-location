package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.data.PlaceRepositoryImpl
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.use_case.GetAllPlacesUseCase
import campus.tech.kakao.map.domain.use_case.GetLogsUseCase
import campus.tech.kakao.map.domain.use_case.GetPlacesUseCase
import campus.tech.kakao.map.domain.use_case.UpdateLogsUseCase
import campus.tech.kakao.map.domain.use_case.RemoveLogUseCase
import campus.tech.kakao.map.domain.use_case.UpdatePlacesUseCase
import com.kakao.vectormap.KakaoMapSdk

class PlaceApplication: Application() {
    private val placeRepository: PlaceRepository by lazy { PlaceRepositoryImpl.getInstance(this) }
    val getLogsUseCase by lazy { GetLogsUseCase(placeRepository) }
    val getPlacesUseCase by lazy { GetPlacesUseCase(placeRepository)}
    val getAllPlacesUseCase by lazy { GetAllPlacesUseCase(placeRepository)}
    val updateLogsUseCase by lazy { UpdateLogsUseCase(placeRepository) }
    val updatePlacesUseCase by lazy { UpdatePlacesUseCase(placeRepository) }
    val removeLogUseCase by lazy {RemoveLogUseCase(placeRepository)}
    override fun onCreate() {
        super.onCreate()
        val key = getString(R.string.kakao_api_key)

        KakaoMapSdk.init(this, key)
    }






}