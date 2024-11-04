package com.sppProject.app.data.data_class

import com.google.gson.annotations.SerializedName

/**
 * This data class represents a buyer.
 * It defines how buyer data will be structured when sent to or received from the API.
 *
 * @param id The unique ID of the buyer.
 * @param name The name of the buyer.
 */
data class Buyer(
    @SerializedName("buyId") val id: Long?,
    @SerializedName("buyName") val name: String
) {
    constructor(name: String) : this(id = null, name = name)
}