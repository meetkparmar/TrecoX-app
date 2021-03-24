package com.bebetterprogrammer.trecox.ui.home

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.adapter.CompanyDetailsAdapter
import com.bebetterprogrammer.trecox.models.Company
import com.bebetterprogrammer.trecox.models.getCompanyInstance
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import java.lang.reflect.Type


class HomeFragment : Fragment() {

    lateinit var currentView: View
    lateinit var database: DatabaseReference
    lateinit var adapter: CompanyDetailsAdapter
    val companyList = mutableListOf<Company>()
    var valueListner: ValueEventListener? = null

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
        valueListner = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                companyList.clear()
                val tableMap = dataSnapshot.getValue<kotlin.collections.HashMap<String, Any>>()
                tableMap?.get("company")?.let {
                    val rows = it as HashMap<String, Any>
                    for (key in rows.keys) {
                        val row = rows[key] as? HashMap<String, String>
                        row?.let {
                            companyList.add(getCompanyInstance(row))
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
        initAdapter()
    }

    private fun initAdapter() {
        rv_company_list.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = CompanyDetailsAdapter()
        rv_company_list.adapter = adapter
    }
}