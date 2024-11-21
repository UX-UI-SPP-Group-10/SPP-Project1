package com.sppProject.app.data

import android.content.Context
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.data.data_class.Company

class UserSessionManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    // Add a method to save Firebase User ID
    fun saveFirebaseUserId(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("firebaseUserId", userId)
        editor.apply()
    }

    // Retrieve Firebase User ID
    fun getFirebaseUserId(): String? {
        return sharedPreferences.getString("firebaseUserId", null)
    }

    // Save buyer information
    fun saveBuyerInfo(buyer: Buyer) {
        val editor = sharedPreferences.edit()
        editor.putLong("buyerId", buyer.id ?: -1L)
        editor.putString("buyerName", buyer.name)
        editor.apply()
    }

    // Retrieve the logged-in buyer
    fun getLoggedInBuyer(): Buyer? {
        val buyerId = sharedPreferences.getLong("buyerId", -1L)
        val buyerName = sharedPreferences.getString("buyerName", null)
        return if (buyerId != -1L && buyerName != null) {
            Buyer(buyerId, buyerName)
        } else {
            null
        }
    }

    // Save company information
    fun saveCompanyInfo(company: Company) {
        val editor = sharedPreferences.edit()
        editor.putLong("companyId", company.id ?: -1L)
        editor.putString("companyName", company.name)
        editor.apply()
    }

    // Retrieve the logged-in company
    fun getLoggedInCompany(): Company? {
        val companyId = sharedPreferences.getLong("companyId", -1L)
        val companyName = sharedPreferences.getString("companyName", null)
        return if (companyId != -1L && companyName != null) {
            Company(companyId, companyName)
        } else {
            null
        }
    }

    // Clear both buyer and company information
    fun clearSessionInfo() {
        sharedPreferences.edit().clear().apply()
    }
}
