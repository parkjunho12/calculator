package com.junho.admob

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val resultPrice = intent.getIntExtra("resultprice", 0)
        val resultString = intent.getStringExtra("resultString")
        val totalPrice = intent.getIntExtra("totalprice", 0)
        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            resources.configuration.locale
        }
        val totalMent =  if (current != Locale.KOREA) {
            resultString + "\n ${getString(R.string.total)}: $${totalPrice.toString()} " +"\n Please pay ${resultPrice.toString()}${getString(R.string.won)} per person!"
        } else {
           resultString + "\n ${getString(R.string.total)}: ${totalPrice.toString()}${getString(R.string.won)} " +"\n 인당 ${resultPrice.toString()}원 씩 내주세요!"
        }
        findViewById<EditText>(R.id.result_edit).setText(totalMent)

        findViewById<Button>(R.id.share_button).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"

            val textToShare =
                totalMent
            shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare)

            val chooser = Intent.createChooser(shareIntent, getString(R.string.share_friend))
            startActivity(chooser)
        }

    }
}