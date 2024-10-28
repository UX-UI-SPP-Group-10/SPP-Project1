package com.sppProject.app.api_integration.data_class

import com.google.gson.annotations.SerializedName

data class Buyer(
    @SerializedName("buyId") val id: Long,
    @SerializedName("buyName") val name: String?
)
