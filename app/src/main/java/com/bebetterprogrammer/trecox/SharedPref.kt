package com.bebetterprogrammer.trecox

import android.content.Context
import android.content.SharedPreferences
import com.bebetterprogrammer.trecox.models.Company

class SharedPref private constructor(context: Context){

    private val fileName = "preference"
    private val pref: SharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    private val configFileName = "config"
    private val configPref: SharedPreferences =
        context.getSharedPreferences(configFileName, Context.MODE_PRIVATE)

    var firstLaunch: Boolean
        get() = pref.getBoolean(FIRST_LAUNCH, true)
        set(value) = pref.edit().putBoolean(FIRST_LAUNCH, value).apply()

    var category: String?
        get() = pref.getString(PREF_KEY_CATEGORY, null)
        set(value) = pref.edit().putString(PREF_KEY_CATEGORY, value).apply()

    var mobileNumber: String?
        get() = pref.getString(MOBILE_NUMBER, null)
        set(value) = pref.edit().putString(MOBILE_NUMBER, value).apply()

    var name: String?
        get() = pref.getString(NAME, null)
        set(value) = pref.edit().putString(NAME, value).apply()

    var address: String?
        get() = pref.getString(ADDRESS, null)
        set(value) = pref.edit().putString(ADDRESS, value).apply()

    var email: String?
        get() = pref.getString(EMAIL, null)
        set(value) = pref.edit().putString(EMAIL, value).apply()

    var pinCode: String?
        get() = pref.getString(PIN_CODE, null)
        set(value) = pref.edit().putString(PIN_CODE, value).apply()

    var shop: String?
        get() = pref.getString(SHOP, null)
        set(value) = pref.edit().putString(SHOP, value).apply()

    var imageUri: String?
        get() = pref.getString(IMAGE_URI, null)
        set(value) = pref.edit().putString(IMAGE_URI, value).apply()

    fun clearData() {
        pref.edit().clear().commit()
    }

    companion object {

        private var sharedPref: SharedPref? = null

        fun getPreference(context: Context): SharedPref {
            return sharedPref ?: synchronized(SharedPref::class) {
                val instance = SharedPref(context)
                sharedPref = instance
                instance
            }
        }
    }
}

private const val FIRST_LAUNCH = "first_launch"

private const val PREF_KEY_CATEGORY = "category"

private const val NAME = "name"

private const val EMAIL = "email"

private const val ADDRESS = "address"

private const val MOBILE_NUMBER = "mobile_number"

private const val IMAGE_URI = "image_uri"

private const val PIN_CODE = "pin_code"

private const val SHOP = "shop"
