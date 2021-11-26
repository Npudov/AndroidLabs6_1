package com.example.androidlabs6_1

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ExecutorServices: AppCompatActivity() {
    var secondsElapsed: Int = 0
    lateinit var textSecondsElapsed: TextView
    private val TAG = "Thread status"
    private val statusActivity = "Activity status"
    private lateinit var executorService: ExecutorService



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        Log.i(statusActivity, "MainActivity: onCreate()")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(SECONDS, secondsElapsed)
        }
        super.onSaveInstanceState(outState)
        Log.i(statusActivity, "MainActivity: onSaveInstanceState()")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.run {
            secondsElapsed = getInt(SECONDS)
        }
        Log.i(statusActivity, "MainActivity: onRestoreInstanceState()")
    }

    override fun onStart() {
        Log.i(TAG, "On start: seconds elapsed = $secondsElapsed")
        executorService = Executors.newFixedThreadPool(1)
        executorService.execute{
            try {
                while (!executorService.isShutdown) {
                    Log.i("MainActivity", "${Thread.currentThread()} is active")
                    textSecondsElapsed.post {
                        textSecondsElapsed.text = getString(R.string.seconds, secondsElapsed++)
                    }
                    Thread.sleep(1000)
                }
            } catch (e: Exception) {}
        }
        Log.i(statusActivity, "MainActivity: onStart()")
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "On stop: seconds elapsed = $secondsElapsed")
        executorService.shutdown()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(statusActivity, "MainActivity: onDestroy()")
        executorService.shutdown()
    }

    companion object {
        const val SECONDS = "seconds"
    }
}