package com.bebetterprogrammer.trecox.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetails(
    var productName: String = "",
    var totalItems: String = "",
    var subCategory: String = "",
    var totalPrice: String = "",
    var date: String = "",
    var imgUrl: String = "",
    var companyName: String = ""
): Parcelable

fun getOrderInstance(data: HashMap<String, String>, companyName: String): OrderDetails {
    return OrderDetails(
        data["productName"]?: "",
        data["totalItems"]?:"",
        data["subCategory"]?: "",
        data["totalPrice"]?: "",
        data["date"] ?: "",
        data["imgUrl"] ?: "",
        companyName
    )
}