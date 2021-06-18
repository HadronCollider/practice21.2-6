package com.makentoshe.androidgithubcitemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater as LayoutInflater

class collectionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        val collections = (0 until 100).map{ "Collection №${it}"}

        val recycleView = findViewById<RecyclerView>(R.id.recycleview_collections)
        recycleView.adapter = RecyclerViewAdapterCollections(collections)
    }
}

class RecyclerViewAdapterCollections(private val collections : List<String>): RecyclerView.Adapter<ViewHolderCollections> ()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCollections {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.collection_item, parent, false)
        return ViewHolderCollections(view)
    }

    override fun onBindViewHolder(holder: ViewHolderCollections, position: Int) {
       holder.checkBox.text = collections[position]
    }

    override fun getItemCount(): Int {
        return collections.size
    }
}

class ViewHolderCollections(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val checkBox: CheckBox = itemView.findViewById<CheckBox>(R.id.checkBox)
    val imageButton: ImageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
}



