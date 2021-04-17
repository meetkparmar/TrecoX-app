package com.bebetterprogrammer.trecox.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bebetterprogrammer.trecox.Constant.PRODUCT_DETAIL
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.models.ProductDetail
import kotlinx.android.synthetic.main.activity_company_detail.*

class ProductOrderActivity : AppCompatActivity() {

    private lateinit var productDetail: ProductDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_order)

        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        productDetail = args!!.getParcelable(PRODUCT_DETAIL)!!

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}