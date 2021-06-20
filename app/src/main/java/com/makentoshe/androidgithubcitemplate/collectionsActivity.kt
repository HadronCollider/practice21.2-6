package com.makentoshe.androidgithubcitemplate

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater as LayoutInflater

class Collection(var text : String, var isSelected : Boolean)
{
}

class CollectionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        val collections = mutableListOf(Collection("Hello", false),
            Collection( "Hello3", false),
            Collection( "Hello2", false))

        val recycleView = findViewById<RecyclerView>(R.id.recycleview_collections)
        recycleView.adapter = RecyclerViewAdapterCollections(this, collections)

        val builderSettings : AlertDialog.Builder = AlertDialog.Builder(this)
        // Settings builder
        val layoutInflater : LayoutInflater = LayoutInflater.from(this)
        val view : View = layoutInflater.inflate(R.layout.collection_settings, null)
        builderSettings.setView(view)
        builderSettings.setCancelable(true);
        builderSettings.setPositiveButton("Add",
            DialogInterface.OnClickListener { dialog, id ->
                val alertDialog : AlertDialog = dialog as AlertDialog
                val newText = alertDialog.findViewById<EditText>(R.id.editTextCollectionName)
                collections.add(Collection(newText?.text.toString(), false ))
                recycleView.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            })
        builderSettings.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()
            })

        val dialogSettings : AlertDialog = builderSettings.create()

        val addingButton = findViewById<Button>(R.id.buttonAdd)
        addingButton.setOnClickListener {
            dialogSettings.show()

            val editText = dialogSettings.findViewById<EditText>(R.id.editTextCollectionName)
            editText?.setText("Enter name")
        }
    }

    fun updateData()
    {

    }

}


class RecyclerViewAdapterCollections(val activity : CollectionsActivity, val collections : MutableList<Collection>): RecyclerView.Adapter<ViewHolderCollections> ()
{
    private val builderSettings : AlertDialog.Builder = AlertDialog.Builder(activity)
    private val builderDeleteConfirm : AlertDialog.Builder = AlertDialog.Builder(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCollections {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.collection_item, parent, false)
        return ViewHolderCollections(view)
    }

    override fun onBindViewHolder(holder: ViewHolderCollections, position: Int) {
        holder.textView.text = collections[position].text
        holder.cardView.setCardBackgroundColor(Color.parseColor("#00FF00"))

        holder.textView.setTextColor(Color.parseColor("#FFFFFF"))
        //holder.imageButton.setOnClickListener{collections[position].onClick()}
        //holder.imageButton.setOnClick

        /* Dialogs */
        val layoutInflater : LayoutInflater = LayoutInflater.from(activity)
        val view : View = layoutInflater.inflate(R.layout.collection_settings, null)

        // Settings builder
        builderSettings.setView(view)
        builderSettings.setCancelable(true);
        builderSettings.setPositiveButton("Apply",
            DialogInterface.OnClickListener { dialog, id ->
                val alertDialog : AlertDialog = dialog as AlertDialog
                val newText = alertDialog.findViewById<EditText>(R.id.editTextCollectionName)
                collections[position].text = newText?.text.toString()
                notifyItemChanged(position)
                dialog.dismiss()
            })
        builderSettings.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})

        val dialogSettings : AlertDialog = builderSettings.create()
        holder.imageButton.setOnClickListener{
            dialogSettings.show()

            val editText = dialogSettings.findViewById<EditText>(R.id.editTextCollectionName)
            editText?.setText(collections[position].text)
        }

        // Delete confirm builder
        builderDeleteConfirm.setMessage("Delete collection?");
        builderDeleteConfirm.setCancelable(true);
        builderDeleteConfirm.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, id ->
                collections.removeAt(position);
                dialogSettings.cancel()
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, collections.size);

            })
        builderDeleteConfirm.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})
        val dialogDeleteConfirm : AlertDialog = builderDeleteConfirm.create()

        val DeleteButton = view.findViewById<Button>(R.id.buttonDelete)
        DeleteButton.setOnClickListener{dialogDeleteConfirm.show()}

        // Checkbox
        holder.checkBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            collections[position].isSelected = isChecked
        }
    }

    override fun getItemCount(): Int {
        return collections.size
    }
}

class ViewHolderCollections(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val checkBox: CheckBox = itemView.findViewById<CheckBox>(R.id.checkBox)
    val textView: TextView = itemView.findViewById<TextView>(R.id.textView7)
    val imageButton: ImageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
    val cardView: CardView = itemView.findViewById<CardView>(R.id.cardView)
}






