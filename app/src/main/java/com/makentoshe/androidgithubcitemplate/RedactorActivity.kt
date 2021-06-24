package com.makentoshe.androidgithubcitemplate

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Tests(var Ques : String, var Ans : String)

class RedactorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redactor)
        if (intent.hasExtra(IntentTags.TITLE_COLLECTION_TO_REDACTOR))
            title = intent.getStringExtra(IntentTags.TITLE_COLLECTION_TO_REDACTOR) + " redactor"
        else
            title = "Collection redactor"

        val tests = (0 until 20).map { Tests("Определение #${it}", "Формулировка #${it}") } as MutableList

        val recycleView = findViewById<RecyclerView>(R.id.redactor_recycler)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = RecyclerViewAdapterRedactor(this, tests)

        val add_button = findViewById<Button>(R.id.redactor_add_button)
        add_button.setOnClickListener {
            val li: LayoutInflater = LayoutInflater.from(this)
            val promptsView: View = li.inflate(R.layout.prompt_red, null)
            val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            mDialogBuilder.setView(promptsView)
            val text1_input = promptsView.findViewById<EditText>(R.id.red_pro_text1_input)
            val text2_input = promptsView.findViewById<EditText>(R.id.red_pro_tex2_input)
            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Save") { dialog, id ->
                        val not = Tests(text1_input?.text.toString(), text2_input?.text.toString())
                        tests.add(not)
                        recycleView.adapter?.notifyDataSetChanged()
                        dialog.cancel()
                    }
                    .setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }
            val alertDialog: AlertDialog = mDialogBuilder.create()
            alertDialog.show()
        }
    }
}


class RecyclerViewAdapterRedactor(val activity: RedactorActivity, val tests: MutableList<Tests>): RecyclerView.Adapter<ViewHolderTests> () {
    private val builderDeleteConfirm : AlertDialog.Builder = AlertDialog.Builder(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTests {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.redactor_item, parent, false)
        return ViewHolderTests(view)
    }

    override fun onBindViewHolder(holder: ViewHolderTests, position: Int) {
        holder.textAns.setText(tests[position].Ques)
        holder.textQues.setText(tests[position].Ans)

        holder.cardview.setOnClickListener {
            val li: LayoutInflater = LayoutInflater.from(activity)
            val promptsView: View = li.inflate(R.layout.prompt_red, null)
            val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
            mDialogBuilder.setView(promptsView)
            val text1_input = promptsView.findViewById<EditText>(R.id.red_pro_text1_input)
            val text2_input = promptsView.findViewById<EditText>(R.id.red_pro_tex2_input)
            text1_input.setText(tests[position].Ques)
            text2_input.setText(tests[position].Ans)
            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Apply") { dialog, id ->
                        tests[position].Ques = text1_input.text.toString()
                        tests[position].Ans = text2_input.text.toString()
                        notifyItemChanged(position)
                        dialog.cancel()
                    }
                    .setNeutralButton("Cancel") { dialog, id -> dialog.cancel() }
                    .setNegativeButton("Delete") {dialog, id ->
                        builderDeleteConfirm.setMessage("Delete card?")
                        builderDeleteConfirm.setCancelable(true)
                        builderDeleteConfirm.setPositiveButton("Yes",
                                DialogInterface.OnClickListener { dialog, id ->
                                    tests.removeAt(position)
                                    dialog.cancel()
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, tests.size)
                                })
                        builderDeleteConfirm.setNegativeButton("Cancel",
                                DialogInterface.OnClickListener { dialog, id -> dialog.dismiss()})
                        val dialogDeleteConfirm : AlertDialog = builderDeleteConfirm.create()
                        dialogDeleteConfirm.show()
                    }
            val alertDialog: AlertDialog = mDialogBuilder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return tests.size
    }
}

class ViewHolderTests(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val cardview = itemView.findViewById<CardView>(R.id.cardViewRedItem)
    val textQues: TextView = itemView.findViewById<EditText>(R.id.first_element)
    val textAns: TextView = itemView.findViewById<EditText>(R.id.second_element)
}