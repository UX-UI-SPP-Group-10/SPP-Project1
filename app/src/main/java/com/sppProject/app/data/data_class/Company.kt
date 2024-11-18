package com.sppProject.app.data.data_class
import com.google.gson.annotations.SerializedName

data class Company (
    @SerializedName("compId") val id: Long?,
    @SerializedName("compName") val name: String,
    @SerializedName("compMail") val mail: String
) {
    constructor(name: String, mail: String) : this(id = null, name = name, mail = mail)
}