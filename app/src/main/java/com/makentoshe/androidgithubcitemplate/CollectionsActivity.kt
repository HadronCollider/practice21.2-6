package com.makentoshe.androidgithubcitemplate

import android.content.Context
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


fun buildColorPickerDialog(context : Context, message : String,
                           posButtonText : String, negButtonText : String,
                           posButton : ColorPickerClickListener) : AlertDialog {
    val builder : ColorPickerDialogBuilder = ColorPickerDialogBuilder.with(context)
    builder.setTitle(message)
    builder.setPositiveButton(posButtonText, posButton)
    builder.setNegativeButton(negButtonText,
        DialogInterface.OnClickListener { dialog, id->
            dialog.dismiss()
        })
    return builder.build()
}

fun buildSettingsDialog(context: Context, view: View,
                        posButtonText : String, negButtonText : String,
                        posButton : DialogInterface.OnClickListener ) : AlertDialog {
    val builder : AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setView(view)
    builder.setCancelable(true);
    builder.setPositiveButton(posButtonText, posButton)
    builder.setNegativeButton(negButtonText,
        DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
        })
    return builder.create()
}

fun buildConfirmDialog(context: Context, message : String,
                       posButtonText : String, negButtonText : String,
                       posButton : DialogInterface.OnClickListener) : AlertDialog {
    val builder : AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setCancelable(true)
    builder.setPositiveButton(posButtonText, posButton)
    builder.setNegativeButton(negButtonText,
        DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})
    return builder.create()
}

fun inputCheck(color : Int, text : String) : Boolean {
    return text.isNotEmpty()
}

class CollectionsActivity : AppCompatActivity() {
    private lateinit var mCollectionItemViewModel: CollectionItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)
        if (intent.hasExtra(IntentTags.TITLE_SUB_TO_COLLECTIONS))
            title = intent.getStringExtra(IntentTags.TITLE_SUB_TO_COLLECTIONS) + " Collections"
        else {
            title = "Collections"
        }

        // Layouts
        val layoutInflater : LayoutInflater = LayoutInflater.from(this)
        val view : View = layoutInflater.inflate(R.layout.collections_adding, null)
        val colorButton = view.findViewById<Button>(R.id.buttonCollectionsAddingColor)
        val addingButton = findViewById<Button>(R.id.buttonCollections)
        val testStartButton = findViewById<Button>(R.id.buttonTestStart)


        // Recycler view
        val recycleView = findViewById<RecyclerView>(R.id.recycleviewCollections)
        val adapter = RecyclerViewAdapterCollections(this)
        recycleView.adapter = adapter

        // Database
        mCollectionItemViewModel = ViewModelProvider(this).get(CollectionItemViewModel::class.java)
        mCollectionItemViewModel.readAllData.observe(this, Observer {
                collections -> adapter.setData(collections)
        })

        // Settings dialog
        val dialogSettings : AlertDialog = buildSettingsDialog(this, view,
            "Add", "Cancel",
            DialogInterface.OnClickListener { dialog, id ->
                val newText = view.findViewById<EditText>(R.id.editTextCollectionsAdding)
                val background = colorButton.background as ColorDrawable

                if (insertDataToDataBase(background.color, newText?.text.toString())) {
                    Toast.makeText(this, "Added!", Toast.LENGTH_LONG).show()
                    recycleView.adapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Input is not correct!", Toast.LENGTH_LONG).show()
                    recycleView.adapter?.notifyDataSetChanged()
                }

                dialog.dismiss()
                })

        // Adding button
        addingButton.setOnClickListener {
            colorButton.setBackgroundColor(Color.parseColor("#FFFFFF"))
            dialogSettings.show()

            val editText = dialogSettings.findViewById<EditText>(R.id.editTextCollectionsAdding)
            editText?.setText("Enter name")
        }

        // Color dialog
        val dialogColor : AlertDialog = buildColorPickerDialog(this, "Choose color", "Select", "Cancel",
            ColorPickerClickListener{ dialog, color, allColors ->
                colorButton.setBackgroundColor(color)
                dialog.dismiss()
            })

        // Color button
        colorButton.setOnClickListener { dialogColor.show() }

        // Test start
        testStartButton.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent) }
    }

    private fun insertDataToDataBase(color : Int, text : String) : Boolean {
        if (inputCheck(color, text)) {
            val collectionItem = CollectionItem(0, color, text)
            mCollectionItemViewModel.addCollectionItem(collectionItem)
            return true
        }
        return false
    }

}


class RecyclerViewAdapterCollections(val activity : CollectionsActivity): RecyclerView.Adapter<ViewHolderCollections> ()
{
    private var collections = emptyList<CollectionItem>()
    private val builderSettings : AlertDialog.Builder = AlertDialog.Builder(activity)
    private val builderDeleteConfirm : AlertDialog.Builder = AlertDialog.Builder(activity)
    lateinit var currentItem : CollectionItem
    private lateinit var mCollectionItemViewModel: CollectionItemViewModel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCollections {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.collections_item, parent, false)
        return ViewHolderCollections(view)
    }

    override fun onBindViewHolder(holder: ViewHolderCollections, position: Int) {
        holder.textView.text = collections[position].text
        holder.cardView.setCardBackgroundColor(collections[position].color)
        holder.textView.setTextColor(Color.parseColor("#000000"))

        // Layouts
        val layoutInflater : LayoutInflater = LayoutInflater.from(activity)
        val view : View = layoutInflater.inflate(R.layout.collections_settings, null)
        val deleteButton = view.findViewById<Button>(R.id.buttonCollectionsSettingsDelete)
        val colorButton = view.findViewById<Button>(R.id.buttonCollectionsSettingsColor)
        val textSettings = view.findViewById<EditText>(R.id.editTextCollectionsSettings)

        // Database
        mCollectionItemViewModel = ViewModelProvider(activity).get(CollectionItemViewModel::class.java)

        // Settings dialog
        val dialogSettings : AlertDialog = buildSettingsDialog(activity, view,
            "Apply", "Cancel",
            DialogInterface.OnClickListener { dialog, id ->
                val newText = view.findViewById<EditText>(R.id.editTextCollectionsSettings)
                val background = colorButton.background as ColorDrawable

                if (updateDataInDataBase(collections[position], background.color, newText?.text.toString())) {
                    Toast.makeText(activity, "Changed!", Toast.LENGTH_LONG).show()
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "Input is not correct!", Toast.LENGTH_LONG).show()
                    notifyDataSetChanged()
                }

                dialog.dismiss()
            })

        // Setting button
        holder.imageButton.setOnClickListener {
            textSettings.setText(collections[position].text)
            colorButton.setBackgroundColor(collections[position].color)
            dialogSettings.show()
        }

        // Color dialog
        val dialogColor : AlertDialog = buildColorPickerDialog(activity, "Choose color","Select", "Cancel",
            ColorPickerClickListener{ dialog, color, allColors ->
                colorButton.setBackgroundColor(color)
                dialog.dismiss()
            })

        // Color button
        colorButton.setOnClickListener { dialogColor.show() }

        // Delete confirm dialog
        val dialogDeleteConfirm : AlertDialog = buildConfirmDialog(activity, "Delete ${collections[position].text}?",
                "Yes", "Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    deleteDataFromDataBase(collections[position])
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, collections.size)

                    dialogSettings.cancel()
                    dialog.dismiss()
                })

        // Delete button
        deleteButton.setOnClickListener { dialogDeleteConfirm.show() }

        // To redactor
        holder.cardView.setOnClickListener {
            val i = Intent(activity, RedactorActivity::class.java)
            i.putExtra(IntentTags.TITLE_COLLECTION_TO_REDACTOR, collections[position].text)
            activity.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return collections.size
    }

    private fun updateDataInDataBase(currentItem : CollectionItem, color : Int, text : String) : Boolean
    {
        if (inputCheck(color, text)) {
            val collectionItem = CollectionItem(currentItem.id, color, text)
            mCollectionItemViewModel.updateCollectionItem(collectionItem)
            return true
        }
        return false
    }

    private fun deleteDataFromDataBase(currentItem : CollectionItem)
    {
        mCollectionItemViewModel.deleteCollectionItem(currentItem)
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






