package com.bebetterprogrammer.trecox.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bebetterprogrammer.trecox.ItemClicked
import com.bebetterprogrammer.trecox.models.ProductDetail

class ProductDetailsAdapter(private val itemClickedListener: ItemClicked) : RecyclerView.Adapter<ProductDetailsViewHolder>() {

    var data = listOf<ProductDetail>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailsViewHolder {
        return ProductDetailsViewHolder.create(parent)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ProductDetailsViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, position, itemClickedListener)
    }

}
