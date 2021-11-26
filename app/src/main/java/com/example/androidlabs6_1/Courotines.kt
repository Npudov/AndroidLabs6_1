package com.example.androidlabs6_1

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class Courotines: AppCompatActivity() {
    var secondsElapsed: Int = 0
    lateinit var textSecondsElapsed: TextView
    private val TAG = "Thread status"
    private val statusActivity = "Activity status"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        Log.i(statusActivity, "MainActivity: onCreate()")
        Log.i(TAG, "On create: seconds elapsed = $secondsElapsed")
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (isActive) {
                    Log.i("MainActivity", "Courotine is active")
                    textSecondsElapsed.post {
                        textSecondsElapsed.text = getString(R.string.seconds, secondsElapsed++)
                    }
                    delay(1000)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(SECONDS, secondsElapsed)
        }
        super.onSaveInstanceState(outState)
        Log.i(statusActivity, "MainActivity: onSaveInstanceState()")
        Log.i(TAG, "onSave: seconds elapsed = $secondsElapsed")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.run {
            secondsElapsed = getInt(SECONDS)
        }
        Log.i(statusActivity, "MainActivity: onRestoreInstanceState()")
        Log.i(TAG, "onRestore: seconds elapsed = $secondsElapsed")
    }

    override fun onStart() {
        Log.i(statusActivity, "MainActivity: onStart()")
        Log.i(TAG, "onStart: seconds elapsed = $secondsElapsed")
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "On stop: seconds elapsed = $secondsElapsed")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(statusActivity, "MainActivity: onDestroy()")
    }

    companion object {
        const val SECONDS = "seconds"
    }
}