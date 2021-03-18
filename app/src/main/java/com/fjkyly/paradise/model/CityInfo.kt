package com.fjkyly.paradise.model
import com.google.gson.annotations.SerializedName


class CityInfo : ArrayList<CityInfo.CityInfoItem>(){
    data class CityInfoItem(
        @SerializedName("city")
        val city: List<City>,
        @SerializedName("name")
        val name: String
    ) {
        data class City(
            @SerializedName("area")
            val area: List<String>,
            @SerializedName("name")
            val name: String
        )
    }
}