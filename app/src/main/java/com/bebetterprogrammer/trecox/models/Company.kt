package com.bebetterprogrammer.trecox.models

data class Company(
    var category: String = "",
    var contact: String = "",
    var displayName: String = "",
    var location: String? = "",
    var email: String? = "",
    var subCategory: List<ProductDetail>? = null
)

fun getCompanyInstance(data: HashMap<String, String>): Company {
    return Company(
        data["category"] ?:"",
        data["contact"] ?:"",
        data["displayName"] ?:"",
        data["location"] ?:"",
        data["email"] ?:""
    )
}
