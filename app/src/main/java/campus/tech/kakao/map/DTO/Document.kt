package campus.tech.kakao.map.DTO

import com.google.gson.annotations.SerializedName

//필요 없는 데이터 주석화
data class Document(
	//val id: String,
	@SerializedName("place_name")
	val placeName: String,
	//val category_name: String,
	//val category_group_code: String,
	@SerializedName("category_group_name")
	val categoryGroupName: String,
	//val phone: String,
	@SerializedName("address_name")
	val addressName: String,
	//val road_address_name: String,
	//val x: String,
	//val y: String,
	//val place_url: String,
	//val distance: String,
)
