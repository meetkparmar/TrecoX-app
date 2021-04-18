package com.bebetterprogrammer.trecox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.models.OrderDetails
import com.squareup.picasso.Picasso

class OrderDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val productName = view.findViewById<TextView>(R.id.tv_product_name)
    private val companyName = view.findViewById<TextView>(R.id.tv_company_name)
    private val category = view.findViewById<TextView>(R.id.tv_category)
    private val totalItem = view.findViewById<TextView>(R.id.tv_total_item)
    private val totalPrice = view.findViewById<TextView>(R.id.tv_total_price)
    private val date = view.findViewById<TextView>(R.id.tv_date)
    private val imgUrl = view.findViewById<ImageView>(R.id.iv_product)

    fun bind(orderDetails: OrderDetails, position: Int) {
        productName.text = orderDetails.productName
        companyName.text = orderDetails.companyName
        category.text = orderDetails.subCategory
        totalItem.text = orderDetails.totalItems
        totalPrice.text = orderDetails.totalPrice
        date.text = orderDetails.date
        if (orderDetails.imgUrl!="") {
            Picasso.with(view.context).load(orderDetails.imgUrl).into(imgUrl)
            imgUrl.clipToOutline = true
        }
    }

    companion object {
        fun create(parent: ViewGroup): OrderDetailViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_details_item, parent, false)
            return OrderDetailViewHolder(view)
        }
    }
}
