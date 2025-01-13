package com.example.educationalapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.educationalapp.network.QuoteResponse
import com.example.educationalapp.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val playButton: Button = findViewById(R.id.play_button)
        val nameEditText: EditText = findViewById(R.id.name)

        // Set an OnClickListener on the play button
        playButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()

            if (name.isNotEmpty()) {
                // Create an intent to start the ActivityPage
                val intent = Intent(this, ActivityPage::class.java).apply {
                    putExtra("USER_NAME", name)
                }
                Bundle().apply {
                    putString(GameFragment.ARG_USERNAME, name)
                }
                startActivity(intent)
            } else {
                // Show a toast message if the name is empty
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }

                R.id.navigation_userstats -> {
                    startActivity(Intent(this, UserStatsActivity::class.java))
                    true
                }

                else -> false
            }
        }


        val quoteTextView: TextView = findViewById(R.id.quote)
        fetchQuote(quoteTextView)
    }

    private fun fetchQuote(quoteTextView: TextView) {
        val call = RetrofitInstance.api.getQuoteOfTheDay()
        call.enqueue(object : Callback<QuoteResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<QuoteResponse>, response: Response<QuoteResponse>) {
                if (response.isSuccessful) {
                    val quoteResponse = response.body()
                    val quote = quoteResponse?.contents?.quotes?.firstOrNull()
                    quoteTextView.text = quote?.quote ?: "No quote available"
                } else {
                    quoteTextView.text = "Failed to load quote: ${response.message()}"
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<QuoteResponse>, t: Throwable) {
                quoteTextView.text = "Failed to load quote: ${t.message}"
            }
        })
    }
}
