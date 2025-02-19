package com.sppProject.app.model.data.data_class

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("itemId") var id: Long?,            // Matches itemId from backend
    @SerializedName("itemName") var name: String,      // Matches itemName from backend
    @SerializedName("price") val price: Int,           // Matches price from backend
    @SerializedName("description") val description: String?, // Matches description from backend
    @SerializedName("stock") val stock: Int,           // Matches stock from backend
    @SerializedName("company") val company: Company?   // Relationship to Company
) {
    constructor(
        name: String,
        price: Int,
        description: String?,
        stock: Int,
    ) : this(id = null, name = name, price = price, description = description, stock = stock, company = null)
}
