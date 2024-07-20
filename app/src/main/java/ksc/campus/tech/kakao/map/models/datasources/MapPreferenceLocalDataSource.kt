package ksc.campus.tech.kakao.map.models.datasources

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.JsonSyntaxException
import com.kakao.vectormap.camera.CameraPosition
import ksc.campus.tech.kakao.map.models.LocationInfo
import java.lang.reflect.Type

class CameraPositionSerializer: JsonSerializer<CameraPosition> {
    override fun serialize(
        src: CameraPosition?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val json = JsonObject()

        if(src == null){
            return json
        }

        json.addProperty("latitude", src.position.latitude)
        json.addProperty("longitude", src.position.longitude)
        json.addProperty("zoom_level",src.zoomLevel)
        json.addProperty("tilt_angle",src.tiltAngle)
        json.addProperty("rotation_angle",src.rotationAngle)
        json.addProperty("height",src.height)

        return json
    }
}

class CameraPositionDeserializer: JsonDeserializer<CameraPosition?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CameraPosition? {
        val nullCheckString = json?.asJsonPrimitive?.asString
        if (nullCheckString.isNullOrEmpty()) {
            return null
        }

        try {
            val jsonObject = json.asJsonObject
            return CameraPosition.from(
                jsonObject.get("latitude").asDouble,
                jsonObject.get("longitude").asDouble,
                jsonObject.get("zoom_level").asInt,
                jsonObject.get("tilt_angle").asDouble,
                jsonObject.get("rotation_angle").asDouble,
                jsonObject.get("height").asDouble
            )
        }
        catch (e: UnsupportedOperationException){
            Log.e("KSC", e.message?:"")
            return null
        }
        catch (e: NumberFormatException){
            Log.e("KSC", e.message?:"")
            return null
        }
        catch(e: IllegalStateException){
            Log.e("KSC", e.message?:"")
            return null
        }
    }
}

class MapPreferenceLocalDataSource {
    private val cameraPositionSerializer: Gson = GsonBuilder()
        .registerTypeAdapter(CameraPositionSerializer::class.java, CameraPositionSerializer())
        .create()
    private val cameraPositionDeserializer: Gson = GsonBuilder()
        .registerTypeAdapter(CameraPositionDeserializer::class.java, CameraPositionDeserializer())
        .create()
    private val gson: Gson = Gson()

    fun getCameraPosition(context: Context): CameraPosition?{
        val sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val data = sharedPreference.getString(CAMERA_POSITION_KEY, "")

        if(data.isNullOrEmpty()) {
            return null
        }

        try {
            return cameraPositionDeserializer.fromJson(data, CameraPosition::class.java)
        } catch (e: JsonSyntaxException) {
            Log.e("KSC", e.message ?: "")
            return null
        }
    }

    fun getSelectedLocation(context: Context): LocationInfo? {
        val sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val data = sharedPreference.getString(SELECTED_LOCATION_KEY, "")

        if (data.isNullOrEmpty()) {
            return null
        }

        try {
            return gson.fromJson(data, LocationInfo::class.java)
        } catch (e: JsonSyntaxException) {
            Log.e("KSC", e.message ?: "")
            return null
        }
    }

    fun saveCameraPosition(context: Context, position: CameraPosition) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(CAMERA_POSITION_KEY, cameraPositionSerializer.toJson(position))
        editor.apply()
    }

    fun saveSelectedLocation(context: Context, location: LocationInfo) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(SELECTED_LOCATION_KEY, gson.toJson(location))
        editor.apply()
    }

    companion object {
        private const val PREFERENCE_NAME = "mapViewPreference"
        private const val CAMERA_POSITION_KEY = "currentPosition"
        private const val SELECTED_LOCATION_KEY = "selectedLocation"
    }
}