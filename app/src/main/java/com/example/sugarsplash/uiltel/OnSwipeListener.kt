package com.example.sugarsplash.uiltel

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

open class OnSwipeListener(context: Context?) : View.OnTouchListener {

    private var gestureDetector: GestureDetector

    override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(motionEvent)
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false

            val yDiff = e2?.y ?: 0f - (e1?.y ?: 0f)
            val xDiff = e2?.x ?: 0f - (e1?.x ?: 0f)

            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                if (Math.abs(xDiff) > SWIPE_THRESHOLD &&
                    Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
                ) {
                    if (xDiff > 0) {
                        onSwipeLeft()
                    } else {
                        onSwipeRight()
                    }
                    result = true
                }
            } else {
                if (Math.abs(yDiff) > SWIPE_THRESHOLD &&
                    Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD
                ) {
                    if (yDiff > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            }
            return result
        }
    }

    open fun onSwipeBottom() {}

    open fun onSwipeTop() {}

    open fun onSwipeLeft() {}

    open fun onSwipeRight() {}

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}



