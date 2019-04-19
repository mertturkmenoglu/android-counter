package com.example.counter

import android.content.Context
import android.content.Intent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var counter: Int
        val inputStream: InputStream

        try {
            applicationContext.openFileInput("counterFile")
        } catch (e: Exception) {
            val outputStream = applicationContext.openFileOutput("counterFile", Context.MODE_PRIVATE)
            outputStream.write(0)
            outputStream.close()
        } finally {
            inputStream = applicationContext.openFileInput("counterFile")
            counter = inputStream.read()
            inputStream.close()
        }

        count.text = counter.toString()

        count.setOnClickListener {
            counter++

            count.text = if (counter < 100) counter.toString() else "∞"
        }

        shareImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_intent_text) + count.text)
            startActivity(Intent.createChooser(intent, getString(R.string.send_to)))
        }

        saveButton.setOnClickListener {
            saveOnClick(counter)
        }

        resetButton.setOnClickListener {
            counter = 0
            count.text = counter.toString()
            saveOnClick(counter)
        }
    }

    private fun saveOnClick(counter: Int) {
        val outputStream = applicationContext.openFileOutput("counterFile", Context.MODE_PRIVATE)
        outputStream.write(counter)
        outputStream.close()
    }

    override fun onDestroy() {
        val c = count.text
        val str = c.toString()
        val counter = Integer.parseInt(str)
        saveOnClick(counter)
        super.onDestroy()
    }
}
