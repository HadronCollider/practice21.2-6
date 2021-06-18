package com.makentoshe.androidgithubcitemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val notis = findViewById<RecyclerView>(R.id.notis)
        notis.layoutManager = LinearLayoutManager(this)
        notis.adapter = CustomRecyclerAdapter(5)
    }
}

class CustomRecyclerAdapter(val strings: Int): RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_notifications, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.time?.setText("${holder.hour + position}:00")
    }

    override fun getItemCount() = strings
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var time: TextView? = null
    var text: TextView? = null
    var switch: Switch? = null
    var del_but: Button? = null

    var hour: Int = 6

    init {
        time = itemView.findViewById(R.id.noti_time)
        text = itemView.findViewById(R.id.noti_text)
        switch = itemView.findViewById(R.id.noti_switch)
        del_but = itemView.findViewById(R.id.noti_del_button)
    }
}