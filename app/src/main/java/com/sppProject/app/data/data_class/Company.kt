package com.sppProject.app.data.data_class
import com.google.gson.annotations.SerializedName

data class Company (
    @SerializedName("compId") val id: Long?,
    @SerializedName("compName") val name: String,
    @SerializedName("firebaseUid") val firebaseUid: String? = null
) {
    constructor(name: String, firebaseUid: String) : this(id = null, name = name, firebaseUid = firebaseUid)
}