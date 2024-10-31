package com.sppProject.app.api_integration.data_class

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("itemId") val id: Long?,            // Matches itemId from backend
    @SerializedName("itemName") val name: String,      // Matches itemName from backend
    @SerializedName("price") val price: Int,           // Matches price from backend
    @SerializedName("description") val description: String?, // Matches description from backend
    @SerializedName("stock") val stock: Int,           // Matches stock from backend
    @SerializedName("company") val company: Company   // Relationship to Company
) {
    constructor(
        name: String,
        price: Int,
        description: String?,
        stock: Int,
        company: Company
    ) : this(id = null, name = name, price = price, description = description, stock = stock, company = company)
}
