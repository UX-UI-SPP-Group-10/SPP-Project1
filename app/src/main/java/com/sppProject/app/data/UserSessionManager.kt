package com.sppProject.app.data

import android.content.Context
import com.sppProject.app.data.data_class.Buyer

class UserSessionManager (private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    fun saveBuyerInfo(buyer: Buyer) {
        val editor = sharedPreferences.edit()
        editor.putLong("buyerId", buyer.id ?: -1L)
        editor.putString("buyerName", buyer.name)
        editor.apply()
    }

    fun getLoggedInBuyer(): Buyer? {
        val buyerId = sharedPreferences.getLong("buyerId", -1L)
        val buyerName = sharedPreferences.getString("buyerName", null)
        return if (buyerId != -1L && buyerName != null) {
            Buyer(buyerId, buyerName)
        } else {
            null
        }
    }

    fun clearBuyerInfo() {
        sharedPreferences.edit().clear().apply()
    }

}