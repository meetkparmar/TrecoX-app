package com.bebetterprogrammer.trecox.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bebetterprogrammer.trecox.Constant.COMPANY
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.models.Company
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_company_detail.*


class CompanyDetailActivity : AppCompatActivity() {

    lateinit var company: Company
    var valueListner: ValueEventListener? = null
    private var progressDialog: ProgressDialog? = null
    private var subCategories: ArrayList<String> = ArrayList()
    var storage: FirebaseStorage? = null

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

        tv_name.text = company.displayName
        tv_email.text = company.email
        tv_mobile_number.text = company.contact
        tv_address.text = company.location

        val adapter = applicationContext?.let { ArrayAdapter(it, R.layout.list_item, subCategories) }
        et_products?.setAdapter(adapter)

        storage = FirebaseStorage.getInstance()
        val storageRef = storage?.reference
        val httpsReference = storage?.getReferenceFromUrl(
            company.logoUrl.toString()
        )
        Picasso.with(this).load(company.logoUrl).into(company_image)
        company_image.clipToOutline = true
    }

    override fun onStart() {
        super.onStart()
    }
}