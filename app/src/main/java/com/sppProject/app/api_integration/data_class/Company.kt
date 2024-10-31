package com.sppProject.app.api_integration.data_class
import com.google.gson.annotations.SerializedName

data class Company (
    @SerializedName("compId") val id: Long,
    @SerializedName("compName") val name: String,
)