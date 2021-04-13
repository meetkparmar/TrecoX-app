package com.bebetterprogrammer.trecox.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetail(
    var productName: String = "",
    var price: String = "",
    var mrp: String = "",
    var description: String? = "",
    var imgUrl: String? = ""
): Parcelable
