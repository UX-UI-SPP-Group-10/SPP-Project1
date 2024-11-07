package com.sppProject.app.data

import android.content.Context
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.data.data_class.Company
import com.sppProject.app.viewModel.UserViewModel

class UserSessionManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    // Save buyer information along with user type
    fun saveBuyerInfo(buyer: Buyer) {
        val editor = sharedPreferences.edit()
        editor.putLong("buyerId", buyer.id ?: -1L)
        editor.putString("buyerName", buyer.name)
        editor.putString("userType", UserViewModel.UserType.BUYER.name) // Save user type
        editor.apply()
    }

    // Retrieve the logged-in buyer and user type
    fun getLoggedInBuyer(): Buyer? {
        val buyerId = sharedPreferences.getLong("buyerId", -1L)
        val buyerName = sharedPreferences.getString("buyerName", null)
        val userType = sharedPreferences.getString("userType", null)

        return if (buyerId != -1L && buyerName != null && userType == UserViewModel.UserType.BUYER.name) {
            Buyer(buyerId, buyerName)
        } else {
            null
        }
    }

    // Save company information along with user type
    fun saveCompanyInfo(company: Company) {
        val editor = sharedPreferences.edit()
        editor.putLong("companyId", company.id ?: -1L)
        editor.putString("companyName", company.name)
        editor.putString("userType", UserViewModel.UserType.COMPANY.name) // Save user type
        editor.apply()
    }

    // Retrieve the logged-in company and user type
    fun getLoggedInCompany(): Company? {
        val companyId = sharedPreferences.getLong("companyId", -1L)
        val companyName = sharedPreferences.getString("companyName", null)
        val userType = sharedPreferences.getString("userType", null)

        return if (companyId != -1L && companyName != null && userType == UserViewModel.UserType.COMPANY.name) {
            Company(companyId, companyName)
        } else {
            null
        }
    }

    // Clear both buyer and company information
    fun clearSessionInfo() {
        sharedPreferences.edit().clear().apply()
    }

    // Retrieve user type
    fun getUserType(): UserViewModel.UserType {
        val userType = sharedPreferences.getString("userType", UserViewModel.UserType.BUYER.name)
        return UserViewModel.UserType.valueOf(userType ?: UserViewModel.UserType.BUYER.name)
    }
}

