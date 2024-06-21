package com.example.sugarsplash.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sugarsplash.MainActivity
import com.example.sugarsplash.R

class ScoreActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var restartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        // Retrieve final score from intent extras
        val finalScore = intent.getIntExtra("FINAL_SCORE", 0)

        // Initialize views
        scoreTextView = findViewById(R.id.scoreTextView)
        restartButton = findViewById(R.id.restartButton)

        // Display final score
        scoreTextView.text = finalScore.toString()

        // Save the score in SharedPreferences if it's the highest
        saveScore(finalScore)

        // Set OnClickListener for the restart button
        restartButton.setOnClickListener {
            // Finish the current activity and start MainActivity again
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun saveScore(score: Int) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // Retrieve the previous highest score
        val previousHighScore = sharedPreferences.getInt("HIGH_SCORE", 0)

        // Check if the current score is higher than the previous highest score
        if (score > previousHighScore) {
            // Update the highest score in SharedPreferences
            editor.putInt("HIGH_SCORE", score)
            editor.apply()

            // Display a message indicating a new high score
            val message = "$score\nNew High Score!"
            val spannableString = SpannableString(message)
            spannableString.setSpan(ForegroundColorSpan(Color.RED), 0, message.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            scoreTextView.gravity = Gravity.CENTER
            scoreTextView.text = spannableString
        } else {
            // Display the final score
            scoreTextView.text = score.toString()
        }
    }


}




