package com.makentoshe.androidgithubcitemplate

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder

class Conspect(var text : String)
{
    var color = Color.parseColor("#FFFFFF")
}

class ConspectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conspect)
        title = "Conspects"

        val conspects = (0 until 100).map { Conspect("Conspect #${it}") } as MutableList

        val recycleView = findViewById<RecyclerView>(R.id.recycleviewConspects)
        recycleView.adapter = RecyclerViewAdapterConspects(this, conspects)

        val builderSettings : AlertDialog.Builder = AlertDialog.Builder(this)
        // Settings builder
        val layoutInflater : LayoutInflater = LayoutInflater.from(this)
        val view : View = layoutInflater.inflate(R.layout.conspects_adding, null)
        val colorButton = view.findViewById<Button>(R.id.buttonConspectsAddingColor)
        colorButton.setBackgroundColor(Color.parseColor("#FFFFFF"))

        builderSettings.setView(view)
        builderSettings.setCancelable(true);
        builderSettings.setPositiveButton("Add",
            DialogInterface.OnClickListener { dialog, id ->
                val newText = view.findViewById<EditText>(R.id.editTextConspectsAdding)
                val conspect = Conspect(newText?.text.toString() )
                conspects.add(conspect)
                val background = colorButton.background as ColorDrawable
                conspect.color = background.color
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
        val addingButton = findViewById<Button>(R.id.buttonConspects)
        addingButton.setOnClickListener {
            dialogSettings.show()

            val editText = dialogSettings.findViewById<EditText>(R.id.editTextConspectsAdding)
            editText?.setText("Enter name")
        }
    }
}


class RecyclerViewAdapterConspects(val activity : ConspectActivity, val conspects : MutableList<Conspect>): RecyclerView.Adapter<ViewHolderConspects> ()
{
    private val builderSettings : AlertDialog.Builder = AlertDialog.Builder(activity)
    private val builderDeleteConfirm : AlertDialog.Builder = AlertDialog.Builder(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderConspects {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conspects_item, parent, false)
        return ViewHolderConspects(view)
    }

    override fun onBindViewHolder(holder: ViewHolderConspects, position: Int) {
        holder.textView.text = conspects[position].text
        holder.cardView.setCardBackgroundColor(conspects[position].color)

        holder.textView.setTextColor(Color.parseColor("#000000"))

        /* Dialogs */
        val layoutInflater : LayoutInflater = LayoutInflater.from(activity)
        val view : View = layoutInflater.inflate(R.layout.conspects_settings, null)
        val deleteButton = view.findViewById<Button>(R.id.buttonConspectsSettingsDelete)
        val colorButton = view.findViewById<Button>(R.id.buttonConspectsSettingsColor)
        colorButton.setBackgroundColor(conspects[position].color)

        // Settings builder
        builderSettings.setView(view)
        builderSettings.setCancelable(true);
        builderSettings.setPositiveButton("Apply",
            DialogInterface.OnClickListener { dialog, id ->
                val newText = view.findViewById<EditText>(R.id.editTextConspectsSettings)
                conspects[position].text = newText?.text.toString()
                val background = colorButton.background as ColorDrawable
                conspects[position].color = background.color
                notifyItemChanged(position)
                dialog.dismiss()
            })
        builderSettings.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})

        val dialogSettings : AlertDialog = builderSettings.create()
        holder.imageButton.setOnClickListener{
            dialogSettings.show()

            val editText = dialogSettings.findViewById<EditText>(R.id.editTextConspectsSettings)
            editText?.setText(conspects[position].text)
        }

        // Delete confirm builder
        builderDeleteConfirm.setMessage("Delete collection?")
        builderDeleteConfirm.setCancelable(true)
        builderDeleteConfirm.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, id ->
                conspects.removeAt(position)
                dialogSettings.cancel()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, conspects.size)

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

    }

    override fun getItemCount(): Int {
        return conspects.size
    }
}

class ViewHolderConspects(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val textView: TextView = itemView.findViewById<TextView>(R.id.textViewConspectsItem)
    val imageButton: ImageButton = itemView.findViewById<ImageButton>(R.id.imageButtonConspectsItem)
    val cardView: CardView = itemView.findViewById<CardView>(R.id.cardViewConspectsItem)
}