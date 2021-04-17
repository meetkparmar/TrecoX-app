package com.bebetterprogrammer.trecox.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetail(
    var productName: String = "",
    var price: String = "",
    var mrp: String = "",
    var description: String? = "",
    var imgUrl: String? = "",
    var subCategory: String? = ""
): Parcelable

fun getProductDetailInstance(data: HashMap<String, String>, subCategory: String?): ProductDetail {
    return ProductDetail(
        data["productName"] as? String ?: "",
        data["price"] as? String ?:"",
        data["mrp"] as? String ?:"",
        data["description"] as? String ?:"",
        data["imgUrl"] as? String ?:"",
        subCategory
    )
}