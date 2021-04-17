package com.bebetterprogrammer.trecox.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bebetterprogrammer.trecox.*
import com.bebetterprogrammer.trecox.Constant.COMPANY
import com.bebetterprogrammer.trecox.models.Company
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_detail)

        val intent = intent
        val args = intent.getBundleExtra("BUNDLE")
        company = args!!.getParcelable(COMPANY)!!
        if (company.subCategory!=null) {
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

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        Picasso.with(this).load(company.logoUrl).into(company_image)
        company_image.clipToOutline = true

        btn_connection.setOnClickListener {
            progressDialog = showLoadingDialog(this, getString(R.string.sending_connection_request))
            sendRequestInUser()
        }
    }

    private fun sendRequestInUser() {
        val map = HashMap<String, Any>()
        map["status"] = getString(R.string.pending)
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
        FirebaseDatabase.getInstance().reference.child("connections_wholesalers")
            .child(name!!)
            .child(company.displayName)
            .setValue(map)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Connection Request Sent!", Toast.LENGTH_SHORT).show()
                dismissLoadingDialog(progressDialog)
            }
            .addOnFailureListener {
                dismissLoadingDialog(progressDialog)
                showErrorToast(applicationContext, R.string.something_wrong_error)
            }
    }

    override fun onStart() {
        super.onStart()
    }
}