package com.junho.admob

import android.content.Intent
import android.os.Build
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

// Remove the line below after defining your own ad unit ID.
private const val TOAST_TEXT = "Test ads are being shown. " +
        "To show live ads, replace the ad unit ID in res/values/strings.xml " +
        "with your own ad unit ID."
private const val START_LEVEL = 1

class MainActivity : AppCompatActivity() {

    private var currentLevel: Int = 0
    private var interstitialAd: InterstitialAd? = null
    private lateinit var nextLevelButton: Button

    private val list = ArrayList<DutchItem>()
    private var peopleCount = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        peopleCount = intent.getIntExtra("person", 1)

        // Create the next level button, which tries to show an interstitial when clicked.
        nextLevelButton = findViewById(R.id.next_level_button)
        nextLevelButton.isEnabled = false
        nextLevelButton.setOnClickListener {
            if (list.isNotEmpty()) {
                showInterstitial()
            } else {
                Toast.makeText(this, "계산이 불가 합니다!", Toast.LENGTH_LONG).show()
            }
        }
        val adapter = MainRecyclerAdapter(list)
        findViewById<RecyclerView>(R.id.main_recycelerView).adapter = adapter

        val mainList = findViewById<LinearLayout>(R.id.main_list_item)
        val plusItem = findViewById<Button>(R.id.plus_item)
        val plus = findViewById<TextView>(R.id.main_plus_but)
        val placeEdit = findViewById<EditText>(R.id.main_place_button)
        val priceEdit = findViewById<EditText>(R.id.main_price_button)
        plusItem.setOnClickListener {
            mainList.visibility = View.VISIBLE
            plusItem.visibility = View.GONE
            plus.visibility = View.VISIBLE
            placeEdit.setText("")
            priceEdit.setText("")
        }

        plus.setOnClickListener {
            mainList.visibility = View.GONE
            plusItem.visibility = View.VISIBLE
            plus.visibility = View.GONE
            if (placeEdit.text.toString() == "" || priceEdit.text.toString() == "") {
                Toast.makeText(this, "빈칸은 계산할 수 없습니다!", Toast.LENGTH_LONG).show()
            } else {
                list.add(DutchItem(placeEdit.text.toString() , priceEdit.text.toString().toInt()))
                findViewById<RecyclerView>(R.id.main_recycelerView).adapter!!.notifyDataSetChanged()
            }
        }

        // Create the text view to show the level number.
        currentLevel = START_LEVEL

        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        interstitialAd = newInterstitialAd()
        loadInterstitial()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.action_settings -> true
                else -> super.onOptionsItemSelected(item)
            }

    private fun newInterstitialAd(): InterstitialAd {
        return InterstitialAd(this).apply {
            if (BuildConfig.DEBUG) {
                adUnitId = getString(R.string.interstitial_ad_unit_id_test)
            } else {
                adUnitId = getString(R.string.interstitial_ad_unit_id)
            }
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    nextLevelButton.isEnabled = true
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    nextLevelButton.isEnabled = true
                }

                override fun onAdClosed() {
                    // Proceed to the next level.
                    goToNextLevel()
                    var resultString = ""
                    var resultPrice = 0
                    val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        resources.configuration.locales[0]
                    } else {
                        resources.configuration.locale
                    }
                    for (lit in list) {
                        if (current != Locale.KOREA) {
                            resultString += " $"
                            resultString += lit.price.toString() + "\n"
                            resultString += " ${getString(R.string.at)} "
                            resultString += lit.place
                            resultPrice += lit.price.toInt()
                        } else {
                            resultString += lit.place
                            resultString += " ${getString(R.string.at)} "
                            resultString += lit.price
                            resultString += " 결제\n"
                            resultPrice += lit.price.toInt()
                        }
                    }

                    val intentNext = Intent(this@MainActivity, ResultActivity::class.java)
                    intentNext.putExtra("resultString", resultString)
                    intentNext.putExtra("resultprice", resultPrice / peopleCount)
                    intentNext.putExtra("totalprice", resultPrice)
                    startActivity(intentNext)
                }
            }
        }
    }

    private fun showInterstitial() {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (interstitialAd?.isLoaded == true) {
            interstitialAd?.show()
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show()
            goToNextLevel()
        }
    }

    private fun loadInterstitial() {
        // Disable the next level button and load the ad.
        nextLevelButton.isEnabled = false
        val adRequest = AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template")
                .build()
        interstitialAd?.loadAd(adRequest)
    }

    private fun goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
        interstitialAd = newInterstitialAd()
        loadInterstitial()
    }
}