package com.example.hw1_obstacleracinggame

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hw1_obstacleracinggame.fragments.HighScoreFragment
import com.example.hw1_obstacleracinggame.fragments.MapFragment

class GameOverActivity : AppCompatActivity() {

    private lateinit var FRAME_list: FrameLayout
    private lateinit var FRAME_map: FrameLayout
    private lateinit var mapFragment: MapFragment
    private lateinit var highScoreFragment: HighScoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        findViews()
        initFragments()
    }

    private fun findViews() {
        FRAME_list = findViewById(R.id.FRAME_list)
        FRAME_map = findViewById(R.id.FRAME_map)
    }

    private fun initFragments() {
        // יצירת Fragment של HighScore עם ניקוד מועבר
        highScoreFragment = HighScoreFragment()
        val bundle = Bundle()
        val score = intent.extras?.getInt("EXTRA_SCORE", 0) ?: 0
        bundle.putInt("EXTRA_SCORE", score)
        highScoreFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .add(R.id.FRAME_list, highScoreFragment)
            .commit()
    }
}
