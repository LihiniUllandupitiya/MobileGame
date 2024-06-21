package com.example.sugarsplash.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.sugarsplash.MainActivity
import com.example.sugarsplash.R

class PlayActivity : AppCompatActivity() {
    private lateinit var playBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val action = supportActionBar
        action?.hide()
        playBtn = findViewById(R.id.playBtn)
        playBtn.setOnClickListener {
            startActivity(Intent(this@PlayActivity, MainActivity::class.java))
        }
    }
}