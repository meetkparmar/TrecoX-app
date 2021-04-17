package com.bebetterprogrammer.trecox.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bebetterprogrammer.trecox.Constant.CONNECTED
import com.bebetterprogrammer.trecox.Constant.PRODUCT_DETAIL
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.models.ProductDetail
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_order.*

class ProductOrderActivity : AppCompatActivity() {

    private lateinit var productDetail: ProductDetail
    private var totalItems: Int = 0
    private var totalPrice: Int = 0
    private var connected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_order)

        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        productDetail = args?.getParcelable(PRODUCT_DETAIL)!!
        connected = args.getBoolean(CONNECTED)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        tv_product_name.text = productDetail.productName
        tv_subCategory.text = productDetail.subCategory
        tv_product_description.text = productDetail.description
        tv_price.text = "₹ ${productDetail.price}"
        tv_mrp.text = "₹ ${productDetail.mrp}"
        tv_total_price.text = "₹ $totalPrice"
        Picasso.with(this).load(productDetail.imgUrl).into(iv_product)
        iv_product.clipToOutline = true

        btn_minus.setOnClickListener {
            if (totalItems>0) {
                totalItems = tv_items.text.toString().toInt()
                totalItems -= 1
                totalPrice -= productDetail.price.toInt()
                tv_total_price.text = "₹ $totalPrice"
                tv_items.text = totalItems.toString()
            }

        }

        btn_plus.setOnClickListener {
            totalItems = tv_items.text.toString().toInt()
            totalItems += 1
            tv_items.text = totalItems.toString()
            totalPrice += productDetail.price.toInt()
            tv_total_price.text = "₹ $totalPrice"
        }

        btn_order.setOnClickListener {
            if (!connected) {
                Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_LONG).show()
            } else {
                if (totalItems == 0) {
                    Toast.makeText(this, "Please Add At least 1 item!", Toast.LENGTH_SHORT).show()
                } else {
                    sendOrder()
                }

            }

        }
    }

    private fun sendOrder() {

    }
}