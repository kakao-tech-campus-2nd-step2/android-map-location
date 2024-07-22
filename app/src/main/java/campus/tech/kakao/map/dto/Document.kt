package campus.tech.kakao.map.dto

import com.google.gson.annotations.SerializedName

data class Document(
	@SerializedName("place_name")
	val placeName: String,
	@SerializedName("category_group_name")
	val categoryGroupName: String,
	@SerializedName("address_name")
	val addressName: String,
	@SerializedName("x")
	val longitude : String,
	@SerializedName("y")
	val latitude: String
)
