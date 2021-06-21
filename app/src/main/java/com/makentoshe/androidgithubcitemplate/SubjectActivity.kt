package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton

class SubjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)
        title = "Предмет"

        val imageButtonCollections = findViewById<ImageButton>(R.id.imageButtonCollections)
        val imageButtonConspects = findViewById<ImageButton>(R.id.imageButtonConspects)

        val buttonAnimation1 = AnimationUtils.loadAnimation(this, R.anim.click)
        val buttonAnimation2 = AnimationUtils.loadAnimation(this, R.anim.click)

        val setDelay : Handler = Handler()

        imageButtonCollections.setOnClickListener {
            val intent = Intent(this, CollectionsActivity::class.java)
            imageButtonCollections.startAnimation(buttonAnimation1)
            imageButtonConspects.isClickable = false
            val startDelay : Runnable  = Runnable {
                startActivity(intent)
                imageButtonConspects.isClickable = true
            }
            setDelay.postDelayed(startDelay, 200)
        }
        imageButtonConspects.setOnClickListener {
            val intent = Intent(this, ConspectActivity::class.java)
            imageButtonConspects.startAnimation(buttonAnimation2)
            imageButtonCollections.isClickable = false

            val startDelay : Runnable  = Runnable {
                imageButtonCollections.isClickable = true
                startActivity(intent)
            }
            setDelay.postDelayed(startDelay, 200)

        }

    }
}