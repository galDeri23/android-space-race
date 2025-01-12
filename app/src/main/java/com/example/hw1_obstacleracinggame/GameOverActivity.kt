package com.example.hw1_obstacleracinggame

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hw1_obstacleracinggame.fragments.HighScoreFragment
import com.example.hw1_obstacleracinggame.fragments.MapFragment
import com.example.hw1_obstacleracinggame.interfaces.callbake_HighScoreItemClicked
import com.google.android.gms.maps.model.LatLng

class GameOverActivity : AppCompatActivity() {

    private lateinit var highScoreFragment: HighScoreFragment
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        initFragments()
    }

    private fun initFragments() {

        mapFragment = MapFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.FRAME_map, mapFragment)
            .commit()


        highScoreFragment = HighScoreFragment()
        highScoreFragment.highScoreItemClicked = object : callbake_HighScoreItemClicked {
            override fun highScoreItemClicked(score: Int, lat: Double, lon: Double) {
                mapFragment.focusOnLocation(LatLng(lat, lon))
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.FRAME_list, highScoreFragment)
            .commit()

        //show all scores on the map
        highScoreFragment.setOnDataReadyListener {
            val allLocations = highScoreFragment.getAllScoresWithLocations()
            mapFragment.setMarkers(allLocations)
        }
    }
}
