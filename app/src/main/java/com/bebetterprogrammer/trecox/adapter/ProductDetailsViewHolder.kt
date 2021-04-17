package com.bebetterprogrammer.trecox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bebetterprogrammer.trecox.ItemClicked
import com.bebetterprogrammer.trecox.R
import com.bebetterprogrammer.trecox.models.ProductDetail
import com.squareup.picasso.Picasso

class ProductDetailsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val productName = view.findViewById<TextView>(R.id.tv_product_name)
    private val productPrice = view.findViewById<TextView>(R.id.tv_price)
    private val productImage = view.findViewById<ImageView>(R.id.product_image)

    fun bind(product: ProductDetail, position: Int, itemClickedListener: ItemClicked) {
        productName.text = product.productName
        productPrice.text = product.price
        Picasso.with(view.context).load(product.imgUrl).into(productImage)
        productImage.clipToOutline = true
        this.view.setOnClickListener {
            itemClickedListener.onClickedListener(position)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ProductDetailsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_details_item, parent, false)
            return ProductDetailsViewHolder(view)
        }
    }
}
