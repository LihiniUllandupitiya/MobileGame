package com.example.sugarsplash
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.sugarsplash.uiltel.OnSwipeListener
import com.example.sugarsplash.view.ScoreActivity
import java.util.Arrays.asList

class MainActivity : AppCompatActivity() {

    // Define a boolean flag to track the first movement of candies
    private var firstMoveCompleted = false

    /** adding candies **/
    var candies = intArrayOf(
        R.drawable.pinkcandy,
        R.drawable.greencandy,
        R.drawable.orangecandy,
        R.drawable.purplecandy,
        R.drawable.redcandy,
        R.drawable.pinksweet
    )

    private lateinit var countDownTimer: CountDownTimer
    private val TIMER_DURATION: Long = 30000 // 30 seconds
    private lateinit var timeTextView: TextView


    var widthOfBlock: Int = 0
    var noOfBlock: Int = 8
    var widthOfScreen: Int = 0
    lateinit var candy: ArrayList<ImageView>
    var candyToBeDragged: Int = 0
    var candyToBeReplaced: Int = 0
    var notCandy: Int = R.drawable.transparent

    lateinit var mHandler: Handler
    private lateinit var scoreResult: TextView
    var score = 0
    var interval = 100L

    // Function to start the countdown timer
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(TIMER_DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timeTextView.text = "$secondsLeft s"
            }

            override fun onFinish() {
                timeTextView.text = "0 s"
                // Navigate to game over page with final score
                val intent = Intent(this@MainActivity, ScoreActivity::class.java)
                intent.putExtra("FINAL_SCORE", score) // Pass final score to game over page
                startActivity(intent)
            }

        }
        countDownTimer.start()
    }

    private fun resetScore() {
        score = 0
        scoreResult.text = "0"
        Log.d("MainActivity", "Score reset to 0")
    }

    private fun displayHighScore() {
        // Retrieve the previous high score from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val previousHighScore = sharedPreferences.getInt("HIGH_SCORE", 0)

        // Find the TextView where you want to display the high score
        val highScoreTextView: TextView = findViewById(R.id.highScoreTextView)

        // Update the text of the TextView with the high score
        highScoreTextView.text = "Previous High Score: $previousHighScore"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resetHighScoreIfNeeded()

        // Find and initialize the TextView for the countdown timer
        timeTextView = findViewById(R.id.timeTextView)

        // Start the countdown timer
        startTimer()


        scoreResult = findViewById(R.id.score)

        // Resetting score to 0 when the game starts
        resetScore()

        // Display the previous high score
        displayHighScore()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        widthOfScreen = displayMetrics.widthPixels

        var heightOfScreen = displayMetrics.heightPixels

        widthOfBlock = widthOfScreen / noOfBlock

        candy = ArrayList()
        createBoard()

        for (imageView in candy) {
            imageView.setOnTouchListener(object : OnSwipeListener(this) {
                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged + 1
                    candyInterChange()

                    if (!firstMoveCompleted) {
                        firstMoveCompleted = true
                    }
                }

                override fun onSwipeRight() {
                    super.onSwipeRight()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged - 1
                    candyInterChange()

                    if (!firstMoveCompleted) {
                        firstMoveCompleted = true
                    }
                }

                override fun onSwipeTop() {
                    super.onSwipeTop()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged - noOfBlock
                    candyInterChange()

                    if (!firstMoveCompleted) {
                        firstMoveCompleted = true
                    }
                }

                override fun onSwipeBottom() {
                    super.onSwipeBottom()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged + noOfBlock
                    candyInterChange()

                    if (!firstMoveCompleted) {
                        firstMoveCompleted = true
                    }
                }
            })

        }

        mHandler = Handler()
        StartRepeat()
    }

    private fun resetHighScoreIfNeeded() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val highScoreReset = sharedPreferences.getBoolean("HIGH_SCORE_RESET", false)

        if (!highScoreReset) {
            // Reset the high score to 0
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt("HIGH_SCORE", 0)
            editor.putBoolean("HIGH_SCORE_RESET", true) // Mark high score as reset
            editor.apply()
        }
    }

    private fun candyInterChange() {
        var background : Int = candy.get(candyToBeReplaced).tag as Int
        var background1 : Int = candy.get(candyToBeDragged).tag as Int

        candy.get(candyToBeDragged).setImageResource(background)
        candy.get(candyToBeReplaced).setImageResource(background1)

        candy.get(candyToBeDragged).setTag(background)
        candy.get(candyToBeReplaced).setTag(background1)
    }
    private fun checkRowForThree() {
        // Only update score if the first movement has occurred
        if (firstMoveCompleted) {
            for (i in 0 until 61) {
                val chosenCandy = candy[i].tag as Int
                val isBlank = candy[i].tag == notCandy
                val notValid = arrayOf(6, 7, 14, 15, 22, 23, 30, 31, 38, 39, 46, 47, 54, 55)
                val list = listOf(*notValid)
                if (!list.contains(i)) {
                    var x = i

                    if (candy.get(x++).tag as Int == chosenCandy
                        && !isBlank
                        && candy.get(x++).tag as Int == chosenCandy
                        && candy.get(x ).tag as Int == chosenCandy
                    ) {
                        score = score + 3
                        scoreResult.text = "$score"
                        candy.get(x).setImageResource(notCandy)
                        candy.get(x).setTag(notCandy)
                        x--
                        candy.get(x).setImageResource(notCandy)
                        candy.get(x).setTag(notCandy)
                        x--
                        candy.get(x).setImageResource(notCandy)
                        candy.get(x).setTag(notCandy)


                    }
                }
            }
            moveDownCandies()
        }}
    private fun checkColumnForThree() {
        // Only update score if the first movement has occurred
        if (firstMoveCompleted) {
            for (i in 0..47) {
                val chosenCandy = candy[i].tag as Int
                val isBlank = candy[i].tag == notCandy
                var x = i

                if (candy.get(x).tag as Int == chosenCandy
                    && !isBlank
                    && candy.get(x + noOfBlock).tag as Int == chosenCandy
                    && candy.get(x + 2 * noOfBlock).tag as Int == chosenCandy
                ) {
                    score = score + 3
                    scoreResult.text = "$score"
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x = x + noOfBlock
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x = x + noOfBlock
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                }
            }
            moveDownCandies()
        }}


    private fun moveDownCandies() {
        val firstRow = arrayOf(1,2,3,4,5,6,7)
        val list = asList(*firstRow)
        for (i in 55 downTo 0){
            if (candy.get(i+noOfBlock).tag as Int == notCandy){
                candy.get(i+noOfBlock).setImageResource(candy.get(i).tag as Int)
                candy.get(i+noOfBlock).setTag(candy.get(i).tag as Int)

                candy.get(i).setImageResource(notCandy)
                candy.get(i).setTag(notCandy)
                if(list.contains(i) && candy.get(i).tag == notCandy){
                    var randomColor :Int = Math.abs(Math.random() * candies.size).toInt()
                    candy.get(i).setImageResource(candies[randomColor])
                    candy.get(i).setTag(candies[randomColor])
                }
            }
        }
        for (i in 0..7){
            if(candy.get(i).tag as Int == notCandy){

                var randomColor :Int = Math.abs(Math.random() * candies.size).toInt()
                candy.get(i).setImageResource(candies[randomColor])
                candy.get(i).setTag(candies[randomColor])
            }
        }

    }
    val repeatChecker :Runnable = object :Runnable{
        override fun run() {
            try {
                checkRowForThree()
                checkColumnForThree()
                moveDownCandies()
            }
            finally {
                mHandler.postDelayed(this,interval)
            }
        }
    }

    private fun StartRepeat() {
        repeatChecker.run()
    }

    private fun createBoard() {
        val gridLayout = findViewById<GridLayout>(R.id.board)
        gridLayout.rowCount = noOfBlock
        gridLayout.columnCount = noOfBlock
        gridLayout.layoutParams.width = widthOfScreen
        gridLayout.layoutParams.height = widthOfScreen

        for (i in 0 until noOfBlock * noOfBlock){
            val imageView = ImageView(this)
            imageView.id = i
            imageView.layoutParams = android.view.ViewGroup.
            LayoutParams(widthOfBlock,widthOfBlock)

            imageView.maxHeight = widthOfBlock
            imageView.maxWidth = widthOfBlock

            var random :Int = Math.floor(Math.random() * candies.size).toInt()

            //randomIndex from candies array
            imageView.setImageResource(candies[random])
            imageView.setTag(candies[random])

            candy.add(imageView)
            gridLayout.addView(imageView)
        }


    }

}