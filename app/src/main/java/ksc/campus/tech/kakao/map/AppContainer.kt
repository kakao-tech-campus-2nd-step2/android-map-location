package ksc.campus.tech.kakao.map

import android.util.Log

class AppContainer {
    val singleTonMap:MutableMap<String, Any> = mutableMapOf()
    inline fun<reified T> addSingleton(implemented:T){
        if (singleTonMap.contains(T::class.java.name)){
            return
        }
        singleTonMap[T::class.java.name] = implemented as Any
    }

    inline fun<reified T> getSingleton():T{
        if(!singleTonMap.contains(T::class.java.name)){
            Log.e("KSC", "can't find corresponding singleton")
            throw IllegalArgumentException()
        }
        return (singleTonMap[T::class.java.name] as T)
    }
}