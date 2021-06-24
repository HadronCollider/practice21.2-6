package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageButton

class SubjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)
        if(intent.hasExtra(IntentTags.TITLE_MENU_TO_SUB))
            title = intent.getStringExtra(IntentTags.TITLE_MENU_TO_SUB)
        else {
            title = "Subject"
        }

        val imageButtonCollections = findViewById<ImageButton>(R.id.imageButtonCollections)
        val imageButtonConspects = findViewById<ImageButton>(R.id.imageButtonConspects)

        val buttonAnimation1 = AnimationUtils.loadAnimation(this, R.anim.click)
        val buttonAnimation2 = AnimationUtils.loadAnimation(this, R.anim.click)

        val setDelay : Handler = Handler()

        imageButtonCollections.setOnClickListener {
            val intentTC = Intent(this, CollectionsActivity::class.java)
            intentTC.putExtra(IntentTags.TITLE_SUB_TO_COLLECTIONS, intent.getStringExtra(IntentTags.TITLE_MENU_TO_SUB))
            imageButtonCollections.startAnimation(buttonAnimation1)
            imageButtonConspects.isClickable = false
            val startDelay : Runnable  = Runnable {
                startActivity(intentTC)
                imageButtonConspects.isClickable = true
            }
            setDelay.postDelayed(startDelay, 200)
        }
        imageButtonConspects.setOnClickListener {
            val intentTC = Intent(this, ConspectActivity::class.java)
            intentTC.putExtra(IntentTags.TITLE_SUB_TO_CONSPECTS, intent.getStringExtra(IntentTags.TITLE_MENU_TO_SUB))
            imageButtonConspects.startAnimation(buttonAnimation2)
            imageButtonCollections.isClickable = false

            val startDelay : Runnable  = Runnable {
                imageButtonCollections.isClickable = true
                startActivity(intentTC)
            }
            setDelay.postDelayed(startDelay, 200)

        }

    }
}