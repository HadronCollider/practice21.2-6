package com.makentoshe.androidgithubcitemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.layout_card.view.*

class testActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        viewPager2.adapter = ViewPagerAdapter()
    }
}

class ViewPagerAdapter : RecyclerView.Adapter<PagerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.layout_card, parent, false))

    override fun getItemCount(): Int = 4

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        cardText.text = "item $position"
    }
}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)