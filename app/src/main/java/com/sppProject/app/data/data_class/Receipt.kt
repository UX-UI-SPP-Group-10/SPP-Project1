package com.sppProject.app.data.data_class

import com.google.gson.annotations.SerializedName

data class Receipt(
    @SerializedName("receiptId") val id: Long?,
    @SerializedName("buyer") val buyer: Buyer,
    @SerializedName("items") val items: List<Item>,
) {
    constructor(
        buyer: Buyer,
        items: List<Item>,
    ) : this(id = null, buyer = buyer, items = items)
}