package com.makentoshe.androidgithubcitemplate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder

class Subject(var text : String)
{
    var color = Color.parseColor("#FFFFFF")
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        val subs = (0 until 20).map { Subject("Sub #${it}") } as MutableList

        val school_sub = findViewById<RecyclerView>(R.id.school_subjects)
        school_sub.layoutManager = LinearLayoutManager(this)
        school_sub.adapter = SchoolSubRecyclerAdapter(this, subs)

        val builderSettings : AlertDialog.Builder = AlertDialog.Builder(this)
        // Settings builder
        val layoutInflater : LayoutInflater = LayoutInflater.from(this)
        val view : View = layoutInflater.inflate(R.layout.prompt_main, null)
        val colorButton = view.findViewById<Button>(R.id.main_pro_color_but)


        builderSettings.setView(view)
        builderSettings.setCancelable(true);
        builderSettings.setPositiveButton("Add",
                DialogInterface.OnClickListener { dialog, id ->
                    val newText = view.findViewById<EditText>(R.id.main_pro_text_input)
                    val sub = Subject(newText?.text.toString() )
                    subs.add(sub)
                    val background = colorButton.background as ColorDrawable
                    sub.color = background.color
                    school_sub.adapter?.notifyDataSetChanged()
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
        val addingButton = findViewById<Button>(R.id.main_add_button)
        addingButton.setOnClickListener {
            colorButton.setBackgroundColor(Color.parseColor("#FFFFFF"))
            view.findViewById<EditText>(R.id.main_pro_text_input).setText("")
            dialogSettings.show()
        }
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

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support librar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "chanel"
            val descriptionText = "noti"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

class SchoolSubRecyclerAdapter(val ctx : MainActivity, val subs : MutableList<Subject>): RecyclerView.Adapter<SchoolSubViewHolder>() {
    private val builderSettings : AlertDialog.Builder = AlertDialog.Builder(ctx)
    private val builderDeleteConfirm : AlertDialog.Builder = AlertDialog.Builder(ctx)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolSubViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        return SchoolSubViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SchoolSubViewHolder, position: Int) {
        holder.textView.text = subs[position].text
        holder.cardView.setCardBackgroundColor(subs[position].color)

        holder.textView.setTextColor(Color.parseColor("#000000"))

        /* Dialogs */
        val layoutInflater : LayoutInflater = LayoutInflater.from(ctx)
        val view : View = layoutInflater.inflate(R.layout.main_item_settings, null)
        val deleteButton = view.findViewById<Button>(R.id.buttonMainSettingsDelete)
        val colorButton = view.findViewById<Button>(R.id.buttonMainSettingsColor)
        colorButton.setBackgroundColor(subs[position].color)

        // Settings builder
        builderSettings.setView(view)
        builderSettings.setCancelable(true);
        builderSettings.setPositiveButton("Apply",
                DialogInterface.OnClickListener { dialog, id ->
                    val newText = view.findViewById<EditText>(R.id.editTextMainSettings)
                    subs[position].text = newText?.text.toString()
                    val background = colorButton.background as ColorDrawable
                    subs[position].color = background.color
                    notifyItemChanged(position)
                    dialog.dismiss()
                })
        builderSettings.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})

        val dialogSettings : AlertDialog = builderSettings.create()
        holder.imageButton.setOnClickListener{
            dialogSettings.show()

            val editText = dialogSettings.findViewById<EditText>(R.id.editTextMainSettings)
            editText?.setText(subs[position].text)
        }

        // Delete confirm builder
        builderDeleteConfirm.setMessage("Delete collection?")
        builderDeleteConfirm.setCancelable(true)
        builderDeleteConfirm.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    subs.removeAt(position)
                    dialogSettings.cancel()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, subs.size)
                    Toast.makeText(ctx, "Subject deleted", Toast.LENGTH_SHORT).show()
                })
        builderDeleteConfirm.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})
        val dialogDeleteConfirm : AlertDialog = builderDeleteConfirm.create()

        deleteButton.setOnClickListener{dialogDeleteConfirm.show()}

        // Color picker
        val builderColorPicker : ColorPickerDialogBuilder = ColorPickerDialogBuilder.with(ctx)
        builderColorPicker.setTitle("Choose color")

        builderColorPicker.setPositiveButton("Select",
                ColorPickerClickListener { dialog, color, allColors ->
                    colorButton.setBackgroundColor(color)
                    dialog.dismiss()
                    notifyItemChanged(position)
                })

        val dialogColor : AlertDialog = builderColorPicker.build()
        colorButton.setOnClickListener{dialogColor.show()}

        holder.cardView.setOnClickListener {
            val i = Intent(ctx, SubjectActivity::class.java)
            i.putExtra(IntentTags.TITLE_MENU_TO_SUB, subs[position].text)
            ctx.startActivity(i)
        }
    }

    override fun getItemCount() = subs.size
}

class SchoolSubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.textViewMenuItem)
    val imageButton: ImageButton = itemView.findViewById(R.id.imageButtonMenuItem)
    val cardView: CardView = itemView.findViewById(R.id.cardViewMenuItem)
}
