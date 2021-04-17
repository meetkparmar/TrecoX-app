package com.bebetterprogrammer.trecox.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bebetterprogrammer.trecox.models.OrderDetails

class OrderDetailAdapter() : RecyclerView.Adapter<OrderDetailViewHolder>() {

    var data = listOf<OrderDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        return OrderDetailViewHolder.create(parent)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, position)
    }

}
