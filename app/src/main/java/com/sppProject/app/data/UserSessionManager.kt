package com.sppProject.app.data

import android.content.Context
import android.content.SharedPreferences
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.data.data_class.Company

class UserSessionManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    fun saveFirebaseUser(uid: String, email: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("firebaseUid", uid)
        editor.putString("firebaseEmail", email)
        editor.apply()
    }

    // Retrieve Firebase user information
    fun getFirebaseUser(): FirebaseUserSession? {
        val uid = sharedPreferences.getString("firebaseUid", null)
        val email = sharedPreferences.getString("firebaseEmail", null)
        return if (uid != null) {
            FirebaseUserSession(uid, email)
        } else {
            null
        }
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
        val buyerMail = sharedPreferences.getString("buyerMail", null )
        return if (buyerId != -1L && buyerName != null && buyerMail != null) {
            Buyer(id = buyerId, name = buyerName, mail =  buyerMail )
        } else {
            null
        }
    }

    // Save company information
    fun saveCompanyInfo(company: Company) {
        val editor = sharedPreferences.edit()
        editor.putLong("companyId", company.id ?: -1L)
        editor.putString("companyName", company.name)
        editor.putString("companyEmail", company.mail) // Add email if applicable
        editor.apply()
    }

    // Retrieve the logged-in company
    fun getLoggedInCompany(): Company? {
        val companyId = sharedPreferences.getLong("companyId", -1L)
        val companyName = sharedPreferences.getString("companyName", null)
        val companyEmail = sharedPreferences.getString("companyEmail", null) // Add email if applicable

        return if (companyId != -1L && companyName != null && companyEmail != null) {
            Company(id = companyId,name = companyName, mail = companyEmail )
        } else {
            null
        }
    }

    // Clear both buyer and company information
    fun clearSessionInfo() {
        sharedPreferences.edit().clear().apply()
    }


}
data class FirebaseUserSession(
    val uid: String,
    val email: String?
)
