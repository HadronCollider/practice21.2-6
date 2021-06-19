package com.makentoshe.androidgithubcitemplate

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        title = "Уведомления"

        val notis = findViewById<RecyclerView>(R.id.notis)
        notis.layoutManager = LinearLayoutManager(this)
        notis.adapter = CustomRecyclerAdapter(10)

        val add_button = findViewById<Button>(R.id.noti_add_button)
        add_button.setOnClickListener {
            makeDialogWindow()
        }
    }

    fun makeDialogWindow () {
        val li: LayoutInflater = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.prompt_noti, null)

        //Создаем AlertDialog
        val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView)

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        val time_input = promptsView.findViewById<EditText>(R.id.noti_pro_time_input)
        val text_input = promptsView.findViewById<EditText>(R.id.noti_pro_text_input)

        mDialogBuilder
            .setCancelable(false)
            .setPositiveButton("Сохранить") { dialog, id ->
                if (text_input != null) {
                    //add_button.setText(text_input.text.toString())                             //TODO
                }
            }
            .setNegativeButton("Отмена") { dialog, id -> dialog.cancel()}
        val alertDialog: AlertDialog = mDialogBuilder.create()
        alertDialog.show()
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
    var switch: SwitchCompat? = null
    var del_but: Button? = null

    var hour: Int = 6

    init {
        time = itemView.findViewById(R.id.noti_time)
        text = itemView.findViewById(R.id.noti_text)
        switch = itemView.findViewById(R.id.noti_switch)
        del_but = itemView.findViewById(R.id.noti_del_button)
    }
}