package com.example.androidlabs6_1

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ExecutorServices: AppCompatActivity() {
    var secondsElapsed: Int = 0
    lateinit var textSecondsElapsed: TextView
    private val TAG = "Thread status"
    private val statusActivity = "Activity status"
    private lateinit var futureBackground: Future<*>



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
        futureBackground = (applicationContext as ForExecutor).executor.submit {
            while (!futureBackground.isCancelled) {
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.seconds, secondsElapsed++)
                }
                Thread.sleep(1000)
            }
        }
        Log.i(statusActivity, "MainActivity: onStart()")
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "On stop: seconds elapsed = $secondsElapsed")
        futureBackground.cancel(true)
        if (futureBackground.isCancelled) {
            Log.i(TAG, "Thread interrupted")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(statusActivity, "MainActivity: onDestroy()")
    }

    companion object {
        const val SECONDS = "seconds"
    }
}

class ForExecutor: Application() {
    val executor: ExecutorService = Executors.newSingleThreadExecutor()
}