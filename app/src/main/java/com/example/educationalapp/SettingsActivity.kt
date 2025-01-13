package com.example.educationalapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var darkModeSwitch: SwitchCompat
    private lateinit var musicSwitch: SwitchCompat
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)

        // Find the views by their IDs
        darkModeSwitch = findViewById(R.id.Darkmode_switch)
        musicSwitch = findViewById(R.id.Music_switch)
        saveButton = findViewById(R.id.Save_button)

        // Load saved settings
        loadSettings()

        // Set up onClick listener for the save button
        saveButton.setOnClickListener {
            saveSettings()
        }

        // Add a listener to the music switch to start/stop music immediately
        musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(Intent(this, MusicService::class.java))
            } else {
                stopService(Intent(this, MusicService::class.java))
            }
        }

        val radioGroup =
            findViewById<RadioGroup>(R.id.radioGroup) // Replace with your RadioGroup ID
        val sharedPrefs = getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedValue = when (checkedId) {
                R.id.question5 -> 5
                R.id.question10 -> 10
                else -> 5 // Default value is 5
            }
            with(sharedPrefs.edit()) {
                putInt("selected_value", selectedValue)
                putInt("selected_radio_button", checkedId) // Save the selected radio button
                apply()
            }
        }

        val savedRadioButtonId = sharedPrefs.getInt("selected_radio_button", R.id.question5)
        radioGroup.check(savedRadioButtonId)
    }

    private fun loadSettings() {
        // Load dark mode and music settings from SharedPreferences
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        val isMusicOn = sharedPreferences.getBoolean("music", true)

        // Set the switch states
        darkModeSwitch.isChecked = isDarkMode
        musicSwitch.isChecked = isMusicOn

        // Apply dark mode based on saved setting
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Control music playback based on saved setting
        if (isMusicOn) {
            startService(Intent(this, MusicService::class.java))
        } else {
            stopService(Intent(this, MusicService::class.java))
        }
    }

    private fun saveSettings() {
        // Get the current states of the switches
        val isDarkMode = darkModeSwitch.isChecked
        val isMusicOn = musicSwitch.isChecked

        // Save the settings to SharedPreferences
        with(sharedPreferences.edit()) {
            putBoolean("dark_mode", isDarkMode)
            putBoolean("music", isMusicOn)
            apply()
        }

        // Apply dark mode
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Control music playback based on switch state
        if (isMusicOn) {
            startService(Intent(this, MusicService::class.java))
        } else {
            stopService(Intent(this, MusicService::class.java))
        }

        // Show a toast message
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()

    }
}
