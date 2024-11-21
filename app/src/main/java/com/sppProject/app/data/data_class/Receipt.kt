package com.sppProject.app.data.data_class

import com.google.gson.annotations.SerializedName

data class Receipt(
    @SerializedName("receiptId") val id: Long?,
    @SerializedName("buyer") val buyer: Buyer,
    @SerializedName("item") val item: Item,
) {
    constructor(
        buyer: Buyer,
        item: Item,
    ) : this(id = null, buyer = buyer, item = item)
}