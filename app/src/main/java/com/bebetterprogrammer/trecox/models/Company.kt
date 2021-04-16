package com.bebetterprogrammer.trecox.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(
    var category: String = "",
    var contact: String = "",
    var displayName: String = "",
    var location: String? = "",
    var email: String? = "",
    var logoUrl: String? = "",
    var subCategory: Map<String, Map<String, HashMap<String, String>>>? = null
): Parcelable

fun getCompanyInstance(data: HashMap<String, Any>): Company {
    return Company(
        data["category"] as? String ?: "",
        data["contact"] as? String ?:"",
        data["displayName"] as? String ?:"",
        data["location"] as? String ?:"",
        data["email"] as? String ?:"",
        data["logoUrl"] as? String ?: "",
        data["subCategory"] as? Map<String, Map<String, HashMap<String, String>>>
    )
}
