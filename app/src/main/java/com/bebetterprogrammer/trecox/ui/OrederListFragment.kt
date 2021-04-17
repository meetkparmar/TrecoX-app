package com.bebetterprogrammer.trecox.ui

import android.app.ProgressDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bebetterprogrammer.trecox.LocalRepository
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.adapter.OrderDetailAdapter
import com.bebetterprogrammer.trecox.dismissLoadingDialog
import com.bebetterprogrammer.trecox.models.OrderCompanyList
import com.bebetterprogrammer.trecox.models.OrderDetails
import com.bebetterprogrammer.trecox.models.getOrderCompanyInstance
import com.bebetterprogrammer.trecox.models.getOrderInstance
import com.bebetterprogrammer.trecox.showLoadingDialog
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_order_list.*

class OrederListFragment : Fragment() {

    lateinit var database: DatabaseReference
    private lateinit var localRepository : LocalRepository
    var valueListner: ValueEventListener? = null
    private var wholesalerName: String? = null
    private var orderCompanies = mutableListOf<OrderCompanyList>()
    private var orderList = mutableListOf<OrderDetails>()
    lateinit var adapter: OrderDetailAdapter
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        database = FirebaseDatabase.getInstance().reference
        wholesalerName = localRepository.getName()
        progressDialog = showLoadingDialog(requireActivity(), "Fetching Order List")
        fetchOrderList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        localRepository = LocalRepository(requireContext())
        initAdapter()
    }

    private fun initAdapter() {
        rv_order_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = OrderDetailAdapter()
        rv_order_list.adapter = adapter
    }

    private fun fetchOrderList() {
        valueListner = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                dismissLoadingDialog(progressDialog)
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dismissLoadingDialog(progressDialog)
                val map = dataSnapshot.child("connections_wholesalers").child(wholesalerName!!).value as? Map<String, Any>
                if (map != null) {
                    for (key in map.keys) {
                        orderCompanies.add(getOrderCompanyInstance(map[key] as HashMap<String, Any>, key))
                    }
                    fetchAllOrders()
                } else {
                    rv_order_list.visibility = View.GONE
                    tv_empty_list.visibility = View.VISIBLE
                }
            }
        }
        valueListner?.let {
            database.addValueEventListener(it)
        }
    }

    private fun fetchAllOrders() {
        for (company in orderCompanies) {
            if (company.orders != null) {
                val companyName = company.companyName
                for (orders in company.orders!!.keys) {
                    val order = company.orders!![orders]
                    orderList.add(getOrderInstance(order!!, companyName))
                    adapter.data = orderList
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}