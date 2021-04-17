package com.bebetterprogrammer.trecox.ui

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bebetterprogrammer.trecox.*
import com.bebetterprogrammer.trecox.Constant.COMPANY_NAME
import com.bebetterprogrammer.trecox.Constant.CONNECTED
import com.bebetterprogrammer.trecox.Constant.PRODUCT_DETAIL
import com.bebetterprogrammer.trecox.models.ProductDetail
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_order.*

class ProductOrderActivity : AppCompatActivity() {

    private lateinit var productDetail: ProductDetail
    private var totalItems: Int = 0
    private var totalPrice: Int = 0
    private var connected: Boolean = false
    private lateinit var localRepository : LocalRepository
    var wholesalerName: String? = null
    var companyName: String? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_order)

        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        productDetail = args?.getParcelable(PRODUCT_DETAIL)!!
        connected = args.getBoolean(CONNECTED)
        companyName = args.getString(COMPANY_NAME)

        localRepository = LocalRepository(this)
        wholesalerName = localRepository.getName()

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
                    progressDialog = showLoadingDialog(this, getString(R.string.ordering_product))
                    sendOrderInUser()
                }
            }
        }
    }

    private fun sendOrderInUser() {
        val map = HashMap<String, Any>()
        map["productName"] = productDetail.productName
        map["subCategory"] = productDetail.subCategory.toString()
        map["totalItems"] = totalItems.toString()
        map["totalPrice"] = totalPrice.toString()
        FirebaseDatabase.getInstance().reference.child("connection_users")
            .child(companyName!!)
            .child(wholesalerName!!)
            .child("orders")
            .push()
            .setValue(map)
            .addOnSuccessListener {
                sendOrderInWholesaler()
            }
            .addOnFailureListener {
                dismissLoadingDialog(progressDialog)
                showErrorToast(this, R.string.something_wrong_error)
            }
    }

    private fun sendOrderInWholesaler() {
        val map = HashMap<String, Any>()
        map["productName"] = productDetail.productName
        map["subCategory"] = productDetail.subCategory.toString()
        map["totalItems"] = totalItems.toString()
        map["totalPrice"] = totalPrice.toString()
        FirebaseDatabase.getInstance().reference.child("connections_wholesalers")
            .child(wholesalerName!!)
            .child(companyName!!)
            .child("orders")
            .push()
            .setValue(map)
            .addOnSuccessListener {
                dismissLoadingDialog(progressDialog)
                Toast.makeText(this, "Order sent Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                dismissLoadingDialog(progressDialog)
                showErrorToast(this, R.string.something_wrong_error)
            }
    }
}