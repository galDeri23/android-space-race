package com.example.hw1_obstacleracinggame

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.hw1_obstacleracinggame.fragments.HighScoreFragment
import com.example.hw1_obstacleracinggame.fragments.MapFragment
import com.example.hw1_obstacleracinggame.interfaces.callbake_HighScoreItemClicked
import com.example.hw1_obstacleracinggame.models.Score
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


class GameOverActivity : AppCompatActivity() {

    private lateinit var highScoreFragment: HighScoreFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastKnownLocation: LatLng? = null
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    private fun getLastLocation() {
        // Check if permissions are granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request missing permissions
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Retrieve last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastKnownLocation = LatLng(location.latitude, location.longitude)
                initFragments()
            } else {
                Log.e("GameOverActivity", "Failed to get location")
                initFragments()
            }
        }.addOnFailureListener {
            Log.e("GameOverActivity", "Error retrieving location: ${it.message}")
            initFragments()
        }
    }

    private fun initFragments() {
        // יצירת האובייקט של MapFragment
        mapFragment = MapFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.FRAME_map, mapFragment)
            .commit()

        // מקבלים את הנתון של השיא מה-Intent
        val score = intent.getIntExtra("EXTRA_SCORE", 0)

        // יצירת Bundle להעברת הנתונים ל-HighScoreFragment
        val bundle = Bundle()
        bundle.putInt("EXTRA_SCORE", score)
        lastKnownLocation?.let {
            bundle.putDouble("EXTRA_LAT", it.latitude)
            bundle.putDouble("EXTRA_LON", it.longitude)
        }

        // יצירת האובייקט של HighScoreFragment והגדרת ה-Callback
        highScoreFragment = HighScoreFragment().apply {
            arguments = bundle
            highScoreItemClicked = object : callbake_HighScoreItemClicked {
                override fun highScoreItemClicked(score: Int, lat: Double, lon: Double) {
                    // קריאה ל-MapFragment כדי לבצע זום על המיקום
                    mapFragment.focusOnLocation(LatLng(lat, lon))
                }
            }
        }

        // החלפת הפרגמנט ב-HighScoreFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.FRAME_list, highScoreFragment)
            .commit()

        // הגדרת ה-Listener לטעינת נתונים מהמפה
        highScoreFragment.setOnDataReadyListener {
            val allLocations = highScoreFragment.getAllScoresWithLocations()
            mapFragment.setMarkers(allLocations)
        }
    }
}