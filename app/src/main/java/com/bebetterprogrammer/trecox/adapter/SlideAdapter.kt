package com.bebetterprogrammer.trecox.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bebetterprogrammer.trecox.R
import kotlinx.android.synthetic.main.slide_layout.view.*

class SlideAdapter(private val context: Context) : PagerAdapter() {

    val business_grow = BitmapFactory.decodeResource(context.resources, R.drawable.business_grow)
    val connection = BitmapFactory.decodeResource(context.resources, R.drawable.connection)
    val communication = BitmapFactory.decodeResource(context.resources, R.drawable.communication)

    var slideImage = arrayOf(
        communication,
        connection,
        business_grow
    )

    var slideTitle = arrayOf(
        "Easy Communication",
        "Business Connections",
        "Grow Together"
    )

    var slideDescription = arrayOf(
        "Direct chat with the company",
        "Connect new company easily for any product at anytime",
        "Grow online with inventory & order management services"
    )

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.slide_layout, container, false)

        view.vp_image.setImageBitmap(slideImage[position])
        view.vp_title.text = slideTitle[position]
        view.vp_description.text = slideDescription[position]

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as ConstraintLayout?)
    }

    override fun getCount(): Int = slideTitle.size
}
