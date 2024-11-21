package com.sppProject.app.data.data_class
import com.google.gson.annotations.SerializedName

data class Company (
    @SerializedName("companyId") val id: Long?,
    @SerializedName("companyName") val name: String,
    @SerializedName("firebaseUid") val firebaseUid: String? = null
) {
    constructor(name: String, firebaseUid: String) : this(id = null, name = name, firebaseUid = firebaseUid)
}