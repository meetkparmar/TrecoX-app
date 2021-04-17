package com.bebetterprogrammer.trecox.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bebetterprogrammer.trecox.*
import com.bebetterprogrammer.trecox.Constant.COMPANY
import com.bebetterprogrammer.trecox.Constant.PRODUCT_DETAIL
import com.bebetterprogrammer.trecox.adapter.ProductDetailsAdapter
import com.bebetterprogrammer.trecox.models.Company
import com.bebetterprogrammer.trecox.models.ProductDetail
import com.bebetterprogrammer.trecox.models.getProductDetailInstance
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_company_detail.*


class CompanyDetailActivity : AppCompatActivity() {

    lateinit var company: Company
    private var progressDialog: ProgressDialog? = null
    private var subCategories: ArrayList<String> = ArrayList()
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    private lateinit var localRepository : LocalRepository
    var name: String? = null
    lateinit var adapter: ProductDetailsAdapter
    private var productList = mutableListOf<ProductDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_detail)

        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        company = args!!.getParcelable(COMPANY)!!
        if (company.subCategory!=null) {
            subCategories.add("All")
            for (subCategory in company.subCategory!!.keys) {
                subCategories.add(subCategory)
            }
        }

        localRepository = LocalRepository(this)
        name = localRepository.getName()

        tv_name.text = company.displayName
        tv_email.text = company.email
        tv_mobile_number.text = company.contact
        tv_address.text = company.location

        val adapter = applicationContext?.let { ArrayAdapter(it, R.layout.list_item, subCategories) }
        et_products?.setAdapter(adapter)

        et_products.setOnItemClickListener { adapterView, view, i, l ->
            val subCategory = et_products.text.toString()
            setProductListInUi(subCategory)
        }

        initAdapter()
        setProductListInUi("All")

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        Picasso.with(this).load(company.logoUrl).into(company_image)
        company_image.clipToOutline = true

        btn_connection.setOnClickListener {
            progressDialog = showLoadingDialog(this, getString(R.string.sending_connection_request))
            sendRequestInUser()
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun sendRequestInUser() {
        val map = HashMap<String, Any>()
        map["status"] = getString(R.string.pending)
        map["category"] = localRepository.getCategory().toString()
        FirebaseDatabase.getInstance().reference.child("connection_users")
            .child(company.displayName)
            .child(name!!)
            .setValue(map)
            .addOnSuccessListener {
                sendRequestInWholesaler()
            }
            .addOnFailureListener {
                dismissLoadingDialog(progressDialog)
                showErrorToast(applicationContext, R.string.something_wrong_error)
            }
    }

    private fun sendRequestInWholesaler() {
        val map = HashMap<String, Any>()
        map["status"] = getString(R.string.pending)
        map["category"] = localRepository.getCategory().toString()
        FirebaseDatabase.getInstance().reference.child("connections_wholesalers")
            .child(name!!)
            .child(company.displayName)
            .setValue(map)
            .addOnSuccessListener {
                dismissLoadingDialog(progressDialog)
                Toast.makeText(applicationContext, "Connection Request Sent!", Toast.LENGTH_SHORT).show()
                btn_connection.isEnabled = false
                btn_connection.isClickable = false
            }
            .addOnFailureListener {
                dismissLoadingDialog(progressDialog)
                showErrorToast(applicationContext, R.string.something_wrong_error)
            }
    }

    private fun initAdapter() {
        rv_products.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ProductDetailsAdapter(ItemClicked { position -> showProductOrderActivity(position) })
        rv_products.adapter = adapter
    }

    private fun setProductListInUi(subCategoryName :String) {
        if (company.subCategory!=null) {
            product_layout.visibility = View.VISIBLE
            rv_products.visibility = View.VISIBLE
            tv_empty_list.visibility = View.GONE
            productList.clear()
            for (subCategory in company.subCategory!!.keys){
                val categoryMap = company.subCategory!![subCategory]
                for (productName in categoryMap!!.keys){
                    val productDetail = categoryMap[productName]
                    if (subCategory == subCategoryName) {
                        productList.add(getProductDetailInstance(productDetail!!, subCategory))
                    } else if (subCategoryName == "All") {
                        productList.add(getProductDetailInstance(productDetail!!, subCategory))
                    }
                }
            }
            adapter.data = productList
            adapter.notifyDataSetChanged()
        } else {
            product_layout.visibility = View.GONE
            rv_products.visibility = View.GONE
            tv_empty_list.visibility = View.VISIBLE
        }
    }

    private fun showProductOrderActivity(position: Int) {
        val intent = Intent(this, ProductOrderActivity::class.java)
        val args = Bundle()
        args.putParcelable(PRODUCT_DETAIL, productList[position])
        intent.putExtra("BUNDLE", args)
        startActivity(intent)
    }
}