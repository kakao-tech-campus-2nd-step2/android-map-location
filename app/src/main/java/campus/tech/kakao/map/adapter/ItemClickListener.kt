package campus.tech.kakao.map.adapter

import android.view.View
import campus.tech.kakao.map.database.KakaoMapItem
import campus.tech.kakao.map.database.SelectMapItem

interface ItemClickListener {
    fun onClick(v: View, selectItem: KakaoMapItem)
}

interface SelectItemClickListener {
    fun onClick(v: View, selectItem: SelectMapItem)
}