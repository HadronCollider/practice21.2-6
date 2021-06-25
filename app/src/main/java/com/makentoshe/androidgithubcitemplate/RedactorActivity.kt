package com.makentoshe.androidgithubcitemplate

import android.content.DialogInterface
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
import com.makentoshe.androidgithubcitemplate.data.*
import kotlin.properties.Delegates

fun inputCheck(testCard : TestCardItem) : Boolean {
    return testCard.firstText.isNotEmpty() && testCard.secondText.isNotEmpty()
}

class RedactorActivity : AppCompatActivity() {
    private lateinit var collectionWithTestCardItemDao : CollectionWithTestCardItemDao
    private lateinit var testCardItemDao : TestCardItemDao
    private var collectionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redactor)

        // Database
        collectionId = intent.getIntExtra(IntentTags.COLLECTION_ID_TO_EDITOR, 0)
        collectionWithTestCardItemDao = TestsDatabase.getDatabase(this).collectionWithTestCardItemDao()
        testCardItemDao = TestsDatabase.getDatabase(this).testCardItemDao()

        val recycleView = findViewById<RecyclerView>(R.id.redactor_recycler)
        recycleView.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerViewAdapterRedactor(this, collectionId)
        recycleView.adapter = adapter

        adapter.setData()

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
                    val testCardItem = TestCardItem(0, text1_input?.text.toString(), text2_input?.text.toString())
                    if (adapter.insertDataToDataBase(testCardItem)) {
                        Toast.makeText(this, "Added!", Toast.LENGTH_LONG).show()
                        adapter.setData()
                    } else {
                        Toast.makeText(this, "Input is not correct!", Toast.LENGTH_LONG).show()
                    }

                    dialog.cancel()
                }
                .setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }
            val alertDialog: AlertDialog = mDialogBuilder.create()
            alertDialog.show()
        }

        title = intent.getStringExtra(IntentTags.TITLE_COLLECTION_TO_EDITOR)
    }

}


class RecyclerViewAdapterRedactor(val activity: RedactorActivity, val collectionId: Int): RecyclerView.Adapter<ViewHolderTests> () {
    private val builderDeleteConfirm : AlertDialog.Builder = AlertDialog.Builder(activity)
    private val collectionWithTestCardItemDao : CollectionWithTestCardItemDao = TestsDatabase.getDatabase(activity).collectionWithTestCardItemDao()
    private val testCardItemDao : TestCardItemDao = TestsDatabase.getDatabase(activity).testCardItemDao()
    private var tests = emptyList<TestCardItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTests {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.redactor_item, parent, false)
        return ViewHolderTests(view)
    }

    override fun onBindViewHolder(holder: ViewHolderTests, position: Int) {
        holder.textAns.setText(tests[position].firstText)
        holder.textQues.setText(tests[position].secondText)

        holder.cardview.setOnClickListener {
            val li: LayoutInflater = LayoutInflater.from(activity)
            val promptsView: View = li.inflate(R.layout.prompt_red, null)
            val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
            mDialogBuilder.setView(promptsView)
            val text1_input = promptsView.findViewById<EditText>(R.id.red_pro_text1_input)
            val text2_input = promptsView.findViewById<EditText>(R.id.red_pro_tex2_input)
            text1_input.setText(tests[position].firstText)
            text2_input.setText(tests[position].secondText)
            mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Apply") { dialog, id ->
                    val testCard = TestCardItem(tests[position].testCardId, text1_input.text.toString(), text2_input.text.toString())
                    if (updateDataInDataBase(testCard)) {
                        Toast.makeText(activity, "Changed!", Toast.LENGTH_LONG).show()
                        setData()
                    } else {
                        Toast.makeText(activity, "Input is not correct!", Toast.LENGTH_LONG).show()
                    }
                    dialog.cancel()
                }
                .setNeutralButton("Cancel") { dialog, id -> dialog.cancel() }
                .setNegativeButton("Delete") {dialog, id ->
                    builderDeleteConfirm.setMessage("Delete card?")
                    builderDeleteConfirm.setCancelable(true)
                    builderDeleteConfirm.setPositiveButton("Yes",
                        DialogInterface.OnClickListener { dialog, id ->
                            deleteDataFromDataBase(tests[position])
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, tests.size)
                            setData()
                            dialog.cancel()
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

    fun setData() {
        val refList : List<CollectionWithTestCardItem> = collectionWithTestCardItemDao.getByCollectionId(collectionId)
        if (!refList.isEmpty()) {
            tests = refList.map { ref ->
                testCardItemDao.getById(ref.testCardId)
            }
        }
        else
            tests = emptyList()

        notifyDataSetChanged()
    }

    fun insertDataToDataBase(testCardItem : TestCardItem) : Boolean {
        if (inputCheck(testCardItem)) {
            val testCardId : Int = testCardItemDao.addTestCardItem(testCardItem).toInt()
            val collectionWithTestCardItem = CollectionWithTestCardItem(collectionId, testCardId)
            collectionWithTestCardItemDao.addCollectionWithTestCardItem(collectionWithTestCardItem)
            return true
        }
        return false
    }

    fun updateDataInDataBase(testCardItem : TestCardItem) : Boolean {
        if (inputCheck(testCardItem)) {
            testCardItemDao.updateTestCardItem(testCardItem)
            return true
        }
        return false
    }

    fun deleteDataFromDataBase(testCardItem: TestCardItem)
    {
        val refList : List<CollectionWithTestCardItem> = collectionWithTestCardItemDao.getByTestCardId(testCardItem.testCardId)
        if (!refList.isEmpty()) {
            refList.forEach { ref -> collectionWithTestCardItemDao.deleteCollectionWithTestCardItem(ref) }
        }

        testCardItemDao.deleteTestCardItem(testCardItem)
    }

}

class ViewHolderTests(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val cardview = itemView.findViewById<CardView>(R.id.cardViewRedItem)
    val textQues: TextView = itemView.findViewById<EditText>(R.id.first_element)
    val textAns: TextView = itemView.findViewById<EditText>(R.id.second_element)
}