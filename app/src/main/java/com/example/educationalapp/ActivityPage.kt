package com.example.educationalapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        // Get the name from the intent
        val name = intent.getStringExtra("USER_NAME")

        // Show a toast message with the name
        Toast.makeText(this, "All the best $name", Toast.LENGTH_LONG).show()

        // Pass the username to the GameFragment
        val gameFragment = GameFragment().apply {
            arguments = Bundle().apply {
                putString(GameFragment.ARG_USERNAME, name)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, gameFragment)
            .commit()

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
    }
}
