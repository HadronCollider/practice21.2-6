package com.makentoshe.androidgithubcitemplate

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.makentoshe.androidgithubcitemplate.data.CollectionItem
import com.makentoshe.androidgithubcitemplate.data.CollectionItemViewModel
import android.view.LayoutInflater as LayoutInflater

class CollectionsActivity : AppCompatActivity() {
    private lateinit var mCollectionItemViewModel: CollectionItemViewModel


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)
        title = "Наборы"

        //val collections = (0 until 100).map { Collection("Collection #${it}", false) } as MutableList

        // Recycler view
        val recycleView = findViewById<RecyclerView>(R.id.recycleviewCollections)
        val adapter = RecyclerViewAdapterCollections(this)
        recycleView.adapter = adapter

        // Database
        mCollectionItemViewModel = ViewModelProvider(this).get(CollectionItemViewModel::class.java)
        mCollectionItemViewModel.readAllData.observe(this, Observer {
                collections -> adapter.setData(collections)
        })

        val builderSettings : AlertDialog.Builder = AlertDialog.Builder(this)
        // Settings builder
        val layoutInflater : LayoutInflater = LayoutInflater.from(this)
        val view : View = layoutInflater.inflate(R.layout.collections_adding, null)
        val colorButton = view.findViewById<Button>(R.id.buttonCollectionsAddingColor)
        colorButton.setBackgroundColor(Color.parseColor("#FFFFFF"))

        builderSettings.setView(view)
        builderSettings.setCancelable(true);
        builderSettings.setPositiveButton("Add",
            DialogInterface.OnClickListener { dialog, id ->
                val newText = view.findViewById<EditText>(R.id.editTextCollectionsAdding)
                val background = colorButton.background as ColorDrawable
                recycleView.adapter?.notifyDataSetChanged()
                dialog.dismiss()
                if (insertDataToDataBase(background.color, newText?.text.toString())) {
                    Toast.makeText(this, "Added!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Input is not correct!", Toast.LENGTH_LONG).show()
                }

            })
        builderSettings.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()
            })

        val dialogSettings : AlertDialog = builderSettings.create()

        // Color picker
        val builderColorPicker : ColorPickerDialogBuilder = ColorPickerDialogBuilder.with(this)
        builderColorPicker.setTitle("Choose color")

        builderColorPicker.setPositiveButton("Select",
            ColorPickerClickListener { dialog, color, allColors ->
                colorButton.setBackgroundColor(color)
                dialog.dismiss()
            })

        val dialogColor : AlertDialog = builderColorPicker.build()
        colorButton.setOnClickListener { dialogColor.show() }

        // Adding button
        val addingButton = findViewById<Button>(R.id.buttonCollections)
        addingButton.setOnClickListener {
            dialogSettings.show()

            val editText = dialogSettings.findViewById<EditText>(R.id.editTextCollectionsAdding)
            editText?.setText("Enter name")
        }

        // Test start
        val testStartButton = findViewById<Button>(R.id.buttonTestStart)
        testStartButton.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)        }
    }


    private fun insertDataToDataBase(color : Int, text : String) : Boolean {
        if (inputCheck(color, text)) {
            val testCard = CollectionItem(0, color, text)
            mCollectionItemViewModel.addTestCard(testCard)
            return true
        }
        return false
    }

    private fun inputCheck(color : Int, text : String) : Boolean {
        return text.isNotEmpty()
    }
}


class RecyclerViewAdapterCollections(val activity : CollectionsActivity): RecyclerView.Adapter<ViewHolderCollections> ()
{
    private var collections = emptyList<CollectionItem>()
    private val builderSettings : AlertDialog.Builder = AlertDialog.Builder(activity)
    private val builderDeleteConfirm : AlertDialog.Builder = AlertDialog.Builder(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCollections {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.collections_item, parent, false)
        return ViewHolderCollections(view)
    }

    override fun onBindViewHolder(holder: ViewHolderCollections, position: Int) {
        holder.textView.text = collections[position].text
        holder.cardView.setCardBackgroundColor(collections[position].color)

        holder.textView.setTextColor(Color.parseColor("#000000"))
        //holder.imageButton.setOnClickListener{collections[position].onClick()}
        //holder.imageButton.setOnClick

        /* Dialogs */
        val layoutInflater : LayoutInflater = LayoutInflater.from(activity)
        val view : View = layoutInflater.inflate(R.layout.collections_settings, null)
        val deleteButton = view.findViewById<Button>(R.id.buttonCollectionsSettingsDelete)
        val colorButton = view.findViewById<Button>(R.id.buttonCollectionsSettingsColor)
        colorButton.setBackgroundColor(collections[position].color)



/*
        // Settings builder
        builderSettings.setView(view)
        builderSettings.setCancelable(true);
        builderSettings.setPositiveButton("Apply",
            DialogInterface.OnClickListener { dialog, id ->
                val newText = view.findViewById<EditText>(R.id.editTextCollectionsSettings)
                collections[position].text = newText?.text.toString()
                val background = colorButton.background as ColorDrawable
                collections[position].color = background.color
                notifyItemChanged(position)
                dialog.dismiss()
            })
        builderSettings.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})

        val dialogSettings : AlertDialog = builderSettings.create()
        holder.imageButton.setOnClickListener{
            dialogSettings.show()

            val editText = dialogSettings.findViewById<EditText>(R.id.editTextCollectionsSettings)
            editText?.setText(collections[position].text)
        }

        // Delete confirm builder
        builderDeleteConfirm.setMessage("Delete collection?")
        builderDeleteConfirm.setCancelable(true)
        builderDeleteConfirm.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, id ->
                collections.removeAt(position)
                dialogSettings.cancel()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, collections.size)

            })
        builderDeleteConfirm.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})
        val dialogDeleteConfirm : AlertDialog = builderDeleteConfirm.create()

        deleteButton.setOnClickListener{dialogDeleteConfirm.show()}

        // Color picker
        val builderColorPicker : ColorPickerDialogBuilder = ColorPickerDialogBuilder.with(activity)
        builderColorPicker.setTitle("Choose color")

        builderColorPicker.setPositiveButton("Select",
            ColorPickerClickListener { dialog, color, allColors ->
                colorButton.setBackgroundColor(color)
                dialog.dismiss()
                notifyItemChanged(position)
            })

        val dialogColor : AlertDialog = builderColorPicker.build()
        colorButton.setOnClickListener{dialogColor.show()}
 */
    }

    override fun getItemCount(): Int {
        return collections.size
    }

    fun setData(newCollections : List<CollectionItem>)
    {
        collections = newCollections
        notifyDataSetChanged()
    }
}

class ViewHolderCollections(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val checkBox: CheckBox = itemView.findViewById<CheckBox>(R.id.checkBoxCollectionsItem)
    val textView: TextView = itemView.findViewById<TextView>(R.id.textViewCollectionsItem)
    val imageButton: ImageButton = itemView.findViewById<ImageButton>(R.id.imageButtonCollectionsItem)
    val cardView: CardView = itemView.findViewById<CardView>(R.id.cardViewCollectionsItem)
}






