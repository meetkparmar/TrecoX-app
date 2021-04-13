package com.bebetterprogrammer.trecox.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bebetterprogrammer.trecox.Constant.COMPANY
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.models.Company

class ComapnyDetailActivity : AppCompatActivity() {

    lateinit var company: Company

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        company = args!!.getParcelable(COMPANY)!!
    }
}