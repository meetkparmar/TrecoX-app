package com.bebetterprogrammer.trecox.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderCompanyList(
    var category: String = "",
    var status: String = "",
    var orders: Map<String, HashMap<String, String>>? = null,
    var companyName: String = ""
): Parcelable

fun getOrderCompanyInstance(data: HashMap<String, Any>, companyName: String): OrderCompanyList {
    return OrderCompanyList(
        data["category"] as? String ?: "",
        data["status"] as? String ?:"",
        data["orders"] as? Map<String, HashMap<String, String>>,
        companyName
    )
}