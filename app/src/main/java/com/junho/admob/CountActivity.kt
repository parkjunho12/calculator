package com.junho.admob

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class CountActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_count)
        val personText = findViewById<TextView>(R.id.person_count)
        findViewById<Button>(R.id.plus_button).setOnClickListener {
            personText.text = (personText.text.toString().toInt() + 1).toString()
        }
        findViewById<Button>(R.id.minus_button).setOnClickListener {
            val count = personText.text.toString().toInt()
            if (count > 1) {
                personText.text = (personText.text.toString().toInt() - 1).toString()
            }
        }
        val nextIntent = Intent(this, MainActivity::class.java)
        findViewById<Button>(R.id.next_button).setOnClickListener{
            nextIntent.putExtra("person",personText.text.toString().toInt())
            startActivity(nextIntent)
        }
    }
}