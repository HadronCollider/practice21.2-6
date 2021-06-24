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
import androidx.recyclerview.widget.RecyclerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import android.view.LayoutInflater as LayoutInflater

class Collection(var text : String, var isSelected : Boolean)
{
    var color = Color.parseColor("#FFFFFF")
}

class CollectionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)
        if (intent.hasExtra(IntentTags.TITLE_SUB_TO_COLLECTIONS))
            title = intent.getStringExtra(IntentTags.TITLE_SUB_TO_COLLECTIONS) + " Collections"
        else {
            title = "Collections"
        }

        val collections = (0 until 10).map { Collection("Collection #${it}", false) } as MutableList

        val recycleView = findViewById<RecyclerView>(R.id.recycleviewCollections)
        recycleView.adapter = RecyclerViewAdapterCollections(this, collections)

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
                val collection = Collection(newText?.text.toString(), false )
                collections.add(collection)
                val background = colorButton.background as ColorDrawable
                collection.color = background.color
                recycleView.adapter?.notifyDataSetChanged()
                dialog.dismiss()
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
}


class RecyclerViewAdapterCollections(val activity : CollectionsActivity, val collections : MutableList<Collection>): RecyclerView.Adapter<ViewHolderCollections> ()
{
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


        // Checkbox
        holder.checkBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            collections[position].isSelected = isChecked
        }

        //to redactor
        holder.cardView.setOnClickListener {
            val i = Intent(activity, RedactorActivity::class.java)
            i.putExtra(IntentTags.TITLE_COLLECTION_TO_REDACTOR, collections[position].text)
            activity.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return collections.size
    }
}

class ViewHolderCollections(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val checkBox: CheckBox = itemView.findViewById<CheckBox>(R.id.checkBoxCollectionsItem)
    val textView: TextView = itemView.findViewById<TextView>(R.id.textViewCollectionsItem)
    val imageButton: ImageButton = itemView.findViewById<ImageButton>(R.id.imageButtonCollectionsItem)
    val cardView: CardView = itemView.findViewById<CardView>(R.id.cardViewCollectionsItem)
}






