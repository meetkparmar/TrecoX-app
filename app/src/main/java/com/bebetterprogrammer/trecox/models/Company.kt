package com.bebetterprogrammer.trecox.models

data class Company(
    var Company: String = "",
    var Contact: String = "",
    var Email: String = "",
    var Location: String? = "",
    var Password: String? = ""
)

fun getCompanyInstance(data: HashMap<String, String>): Company {
    return Company(data.get("Company")?:"",
        data.get("Contact")?:"",
        data.get("Email")?:"",
        data.get("Location")?:"",
        data.get("Password")?:"")
}
