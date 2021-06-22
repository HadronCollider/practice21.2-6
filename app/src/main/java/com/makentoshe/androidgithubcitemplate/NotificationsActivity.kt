package com.makentoshe.androidgithubcitemplate

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TimePicker.OnTimeChangedListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Noti(var text : String, var time: String, var isSelected : Boolean)
{
    var color = Color.parseColor("#FFFFFF")
}

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        title = "Notifications"

        val notis = (0 until 5).map { Noti("Notification #${it}", "0${it}000", false) } as MutableList

        val notis_recycler = findViewById<RecyclerView>(R.id.notis)
        notis_recycler.layoutManager = LinearLayoutManager(this)
        notis_recycler.adapter = NotiRecyclerAdapter(notis, this)

        val add_button = findViewById<Button>(R.id.noti_add_button)
        add_button.setOnClickListener {
            val li: LayoutInflater = LayoutInflater.from(this)
            val promptsView: View = li.inflate(R.layout.prompt_noti, null)
            val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            mDialogBuilder.setView(promptsView)
            val time_input = promptsView.findViewById<TimePicker>(R.id.noti_time_input)
            val text_input = promptsView.findViewById<EditText>(R.id.noti_pro_text_input)
            time_input.setIs24HourView(true)
            time_input.setCurrentHour(12)
            time_input.setCurrentMinute(0)
            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Сохранить") { dialog, id ->
                        var h = time_input.hour.toString()                                       //РАБОТАЕТ НЕ ТРОГАЙ
                        var m = time_input.minute.toString()
                        if (h.length == 1){
                            h = "0" + h
                        }
                        if (m.length == 1){
                            m = "0" + m
                        }
                        val not = Noti(text_input?.text.toString(), h + m, false)
                        notis.add(not)
                        notis_recycler.adapter?.notifyDataSetChanged()
                        dialog.cancel()

                    }
                    .setNegativeButton("Отмена") { dialog, id -> dialog.cancel()}
            val alertDialog: AlertDialog = mDialogBuilder.create()
            alertDialog.show()
        }
    }
}

class NotiRecyclerAdapter(val notis: MutableList<Noti>, val ctx: NotificationsActivity): RecyclerView.Adapter<NotiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_notifications, parent, false)
        return NotiViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        holder.text?.text = notis[position].text
        holder.time?.text = "${notis[position].time[0]}${notis[position].time[1]}:${notis[position].time[2]}${notis[position].time[3]}"

        holder.del_but?.setOnClickListener {
            val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(ctx)
            mDialogBuilder
                    .setCancelable(false)
                    .setMessage("Delete Notification")
                    .setPositiveButton("Delete") { dialog, id ->
                        notis.removeAt(position)
                        dialog.cancel()
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, notis.size)
                    }
                    .setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }
            val alertDialog: AlertDialog = mDialogBuilder.create()
            alertDialog.show()
        }
        holder.switch?.setOnCheckedChangeListener { compoundButton, b ->
            notis[position].isSelected = b
        }
        holder.lay?.setOnClickListener{
            val li: LayoutInflater = LayoutInflater.from(ctx)
            val promptsView: View = li.inflate(R.layout.prompt_noti, null)
            val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(ctx)
            mDialogBuilder.setView(promptsView)
            val time_input = promptsView.findViewById<TimePicker>(R.id.noti_time_input)
            val text_input = promptsView.findViewById<EditText>(R.id.noti_pro_text_input)

            text_input.setText(notis[position].text)

            time_input.setIs24HourView(true)
            time_input.setCurrentHour("${notis[position].time[0]}${notis[position].time[1]}".toInt())
            time_input.setCurrentMinute("${notis[position].time[2]}${notis[position].time[3]}".toInt())
            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Сохранить") { dialog, id ->
                        var h = time_input.hour.toString()                                       //РАБОТАЕТ НЕ ТРОГАЙ
                        var m = time_input.minute.toString()
                        if (h.length == 1){
                            h = "0" + h
                        }
                        if (m.length == 1){
                            m = "0" + m
                        }
                        notis[position].text = text_input.text.toString()
                        notis[position].time = h + m
                        notifyItemChanged(position)
                        dialog.cancel()

                    }
                    .setNegativeButton("Отмена") { dialog, id -> dialog.cancel()}
            val alertDialog: AlertDialog = mDialogBuilder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount() = notis.size
}

class NotiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var time: TextView? = null
    var text: TextView? = null
    var switch: Switch? = null
    var del_but: Button? = null
    var lay: ConstraintLayout? = null

    init {
        time = itemView.findViewById(R.id.noti_time)
        text = itemView.findViewById(R.id.noti_text)
        switch = itemView.findViewById(R.id.noti_switch)
        del_but = itemView.findViewById(R.id.noti_del_button)
        lay = itemView.findViewById(R.id.noti_layout)
    }
}