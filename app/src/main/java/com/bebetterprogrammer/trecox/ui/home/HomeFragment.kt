package com.bebetterprogrammer.trecox.ui.home

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bebetterprogrammer.trecox.*
import com.bebetterprogrammer.trecox.Constant.COMPANY
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.adapter.CompanyDetailsAdapter
import com.bebetterprogrammer.trecox.models.Company
import com.bebetterprogrammer.trecox.models.getCompanyInstance
import com.bebetterprogrammer.trecox.ui.ProductDetailActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    lateinit var currentView: View
    lateinit var database: DatabaseReference
    lateinit var adapter: CompanyDetailsAdapter
    val companyList = mutableListOf<Company>()
    var valueListner: ValueEventListener? = null
    private var progressDialog: ProgressDialog? = null
    private var category: String? = ""
    private lateinit var localRepository : LocalRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseDatabase.getInstance().reference
        currentView = inflater.inflate(R.layout.fragment_home, container, false)
        return currentView
    }

    override fun onStart() {
        super.onStart()
        category = localRepository.getCategory()
        valueListner = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                dismissLoadingDialog(progressDialog)
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                companyList.clear()
                val tableMap = dataSnapshot.getValue<HashMap<String, Any>>()
                tableMap?.get("users")?.let {
                    dismissLoadingDialog(progressDialog)
                    val rows = it as HashMap<String, Any>
                    for (key in rows.keys) {
                        val row = rows[key] as? HashMap<String, String>
                        row?.let {
                            if (getCompanyInstance(row).category == category) {
                                companyList.add(getCompanyInstance(row))
                            }
                        }
                    }

                    if (companyList.isEmpty()) {
                        tv_empty_list.visibility = View.VISIBLE
                        rv_company_list.visibility = View.GONE
                    } else {
                        tv_empty_list.visibility = View.GONE
                        rv_company_list.visibility = View.VISIBLE
                        adapter.data = companyList
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
        valueListner?.let {
            database.addValueEventListener(it)
        }
    }

    override fun onStop() {
        valueListner?.let {
            database.removeEventListener(it)
        }
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        localRepository = LocalRepository(requireContext())
        initAdapter()
        progressDialog = showLoadingDialog(requireActivity(), "Fetching Company List")
    }

    private fun initAdapter() {
        rv_company_list.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = CompanyDetailsAdapter(ItemClicked { position -> showProductDetailFragment(position) })
        rv_company_list.adapter = adapter
    }

    private fun showProductDetailFragment(position: Int) {
        val intent = Intent(context, ProductDetailActivity::class.java)
        val args = Bundle()
        args.putParcelable(COMPANY, companyList[position])
        intent.putExtra("BUNDLE", args)
        startActivity(intent)
    }
}