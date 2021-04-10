package com.bebetterprogrammer.trecox

import android.content.Context

class LocalRepository(private val context: Context) {

    private val sharedPref = SharedPref.getPreference(context)

    fun isFirstLaunch(value: Boolean){
        sharedPref.firstLaunch = value
    }

    fun isFirstLaunch() : Boolean {
        return sharedPref.firstLaunch
    }

    fun setCategory(value: String){
        sharedPref.category = value
    }

    fun getCategory() : String?{
        return sharedPref.category
    }

    fun setName(value: String){
        sharedPref.name = value
    }

    fun getName() : String?{
        return sharedPref.name
    }

    fun setEmail(value: String){
        sharedPref.email = value
    }

    fun getEmail() : String?{
        return sharedPref.email
    }

    fun setMobileNumber(value: String){
        sharedPref.mobileNumber = value
    }

    fun getMobileNumber() : String?{
        return sharedPref.mobileNumber
    }

    fun setAddress(value: String){
        sharedPref.address = value
    }

    fun getAddress() : String?{
        return sharedPref.address
    }

    fun setPinCode(value: String){
        sharedPref.pinCode = value
    }

    fun getPinCode() : String?{
        return sharedPref.pinCode
    }

    fun setShop(value: String){
        sharedPref.shop = value
    }

    fun getShop() : String?{
        return sharedPref.shop
    }

    fun setImageUri(value: String){
        sharedPref.imageUri = value
    }

    fun getImageUri() : String?{
        return sharedPref.imageUri
    }
}