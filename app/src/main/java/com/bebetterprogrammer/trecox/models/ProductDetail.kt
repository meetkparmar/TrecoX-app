package com.bebetterprogrammer.trecox.models

data class ProductDetail(
    var productName: String = "",
    var price: String = "",
    var mrp: String = "",
    var description: String? = "",
    var imgUrl: String? = ""
)
