package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton

class SubjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)

        val imageButtonCollections = findViewById<ImageButton>(R.id.imageButtonCollections)
        val imageButtonConspects = findViewById<ImageButton>(R.id.imageButtonConspects)

        val buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.click)

        imageButtonCollections.setOnClickListener {
            val intent = Intent(this, CollectionsActivity::class.java)
            startActivity(intent)
            imageButtonCollections.startAnimation(buttonAnimation)
        }
        imageButtonConspects.setOnClickListener {
            val intent = Intent(this, CollectionsActivity::class.java)
            startActivity(intent)
            imageButtonConspects.startAnimation(buttonAnimation)
        }

    }
}