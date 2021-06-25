package com.makentoshe.androidgithubcitemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        title = "Test"

        val conspects = (0 until 4).map { Tests("Question ${it}", "Answer $it") } as MutableList

        var i = 0
        var c = 0

        findViewById<TextView>(R.id.card_element).setText(conspects[i].Ques)

        val pbeKey =  findViewById<ProgressBar>(R.id.progressBar)
        pbeKey.setMax(100)

        findViewById<Button>(R.id.button_right).setOnClickListener {
            if (i + 1 < conspects.size)
                i++
            else
                i = 0
            c++
            pbeKey.setProgress(c)
            findViewById<TextView>(R.id.progressNums).setText("$c/100")
            findViewById<TextView>(R.id.card_element).setText(conspects[i].Ques)
        }

        findViewById<Button>(R.id.button_false).setOnClickListener {
            if (i + 1 < conspects.size)
                i++
            else
                i = 0
            findViewById<TextView>(R.id.card_element).setText(conspects[i].Ques)
        }

        findViewById<Button>(R.id.button_check).setOnClickListener {
            val txt = findViewById<EditText>(R.id.card_answer).text.toString()
            if (txt == conspects[i].Ans)
                Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "False", Toast.LENGTH_SHORT).show()

        }
    }
}