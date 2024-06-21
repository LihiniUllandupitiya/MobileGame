package com.example.sugarsplash.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.sugarsplash.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val action = supportActionBar
        action?.hide()
        try{
            Handler().postDelayed({
                startActivity(Intent
                    (this@WelcomeActivity,
                    PlayActivity::class.java))
            }, 5000)
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }
}