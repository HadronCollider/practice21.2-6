package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val school_sub  = findViewById<RecyclerView>(R.id.school_subjects)
        school_sub.layoutManager = LinearLayoutManager(this)
        school_sub.adapter = SchoolSubRecyclerAdapter(10)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.noti_but -> {
                val intent = Intent(this, NotificationsActivity::class.java)
                startActivity(intent)
            }
            R.id.info_but -> {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

class SchoolSubRecyclerAdapter(val strings: Int): RecyclerView.Adapter<SchoolSubViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolSubViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        return SchoolSubViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SchoolSubViewHolder, position: Int) {
       // holder.name?.setText(holder.name.toString())
    }

    override fun getItemCount() = strings
}

class SchoolSubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView? = null

    init {
        name = itemView.findViewById(R.id.school_sub_name)
    }
}
