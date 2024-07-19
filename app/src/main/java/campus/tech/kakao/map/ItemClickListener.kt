package campus.tech.kakao.map

import android.view.View

interface ItemClickListener {
    fun onClick(v: View, selectItem: KakaoMapItem)
}

interface SelectItemClickListener {
    fun onClick(v: View, selectItem: SelectMapItem)
}