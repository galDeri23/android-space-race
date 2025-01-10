package com.example.hw1_obstacleracinggame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class MenuActivity : AppCompatActivity() {


    private lateinit var startGame_IMG_spaceShip: AppCompatImageView

    private lateinit var startGame_TXT_name: MaterialTextView

    private lateinit var startGame_BTN_start: MaterialButton

    private lateinit var startGame_switch_sensor: SwitchMaterial

    private lateinit var startGame_switch_speed:SwitchMaterial

    private lateinit var startGame_BTN_highScores:MaterialButton

    private lateinit var startGame_TXT_speed_status: MaterialTextView

    private lateinit var startGame_TXT_sensor_status: MaterialTextView

    private lateinit var mainActivity: MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViews()
        initViews()

    }

    private fun initViews() {
        startGame_switch_speed.setOnCheckedChangeListener { _, isChecked ->
            updateSpeedText(isChecked)
            changeSpeed()
        }
        startGame_switch_sensor.setOnCheckedChangeListener { _, isChecked ->
            updateSensorText(isChecked)
            changeSensor()
        }
        startGame_BTN_start.setOnClickListener({ startGame() })
        startGame_BTN_highScores.setOnClickListener({ showHighScores() })


    }

    private fun updateSensorText(checked: Boolean) {
        startGame_TXT_sensor_status.text = if (checked) "Tilt" else "Button"
    }

    private fun updateSpeedText(checked: Boolean) {
        startGame_TXT_speed_status.text = if (checked) "Fast" else "Slow"
    }

    private fun changeSensor() {
        val isSensorEnabled = startGame_switch_sensor.isChecked
        // Save to SharedPreferences
        val sharedPref = getSharedPreferences("GameSettings", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("SensorEnabled", isSensorEnabled)
            apply()
        }
    }

    private fun changeSpeed() {
        val isFastSpeed = startGame_switch_speed.isChecked
        // Save to SharedPreferences
        val sharedPref = getSharedPreferences("GameSettings", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("FastSpeed", isFastSpeed)
            apply()
        }
    }

    private fun showHighScores() {
        TODO("Not yet implemented")
    }
    private fun startGame() {

        val isSensorEnabled = startGame_switch_sensor.isChecked
        val isFastSpeed = startGame_switch_speed.isChecked

        val bundle = Bundle()
        bundle.putBoolean("SensorEnabled", isSensorEnabled)
        bundle.putBoolean("FastSpeed", isFastSpeed)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun findViews() {
        startGame_BTN_start = findViewById(R.id.startGame_BTN_start)
        startGame_IMG_spaceShip = findViewById(R.id.startGame_IMG_spaceShip)
        startGame_TXT_name = findViewById(R.id.startGame_TXT_name)
        startGame_switch_sensor = findViewById(R.id.startGame_switch_sensor)
        startGame_TXT_sensor_status = findViewById(R.id.startGame_TXT_sensor_status)
        startGame_switch_speed = findViewById(R.id.startGame_switch_speed)
        startGame_TXT_speed_status = findViewById(R.id.startGame_TXT_speed_status)
        startGame_BTN_highScores = findViewById(R.id.startGame_BTN_highScores)
    }
}