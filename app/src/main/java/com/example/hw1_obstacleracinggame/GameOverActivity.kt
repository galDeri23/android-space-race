package com.example.hw1_obstacleracinggame

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hw1_obstacleracinggame.fragments.HighScoreFragment
import com.example.hw1_obstacleracinggame.fragments.MapFragment
import com.example.hw1_obstacleracinggame.interfaces.callbake_HighScoreItemClicked

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
        handleIncomingScore()
    }

    private fun findViews() {
        FRAME_list = findViewById(R.id.FRAME_list)
        FRAME_map = findViewById(R.id.FRAME_map)
    }

    private fun initFragments() {
//        mapFragment = MapFragment()
//        supportFragmentManager.beginTransaction()
//            .add(R.id.FRAME_map, mapFragment)
//            .commit()


        highScoreFragment = HighScoreFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.FRAME_list, highScoreFragment)
            .commit()
        highScoreFragment.setCallBack(object : callbake_HighScoreItemClicked {
            override fun highScoreItemClicked(score: Int) {

                mapFragment.zoom(0.0, 0.0)
            }
        })


    }

    private fun handleIncomingScore() {
        val score = intent.getIntExtra("EXTRA_SCORE", 0)
        if (score > 0) {
            addScoreToHighScoreFragment(score)
        }
    }

    private fun addScoreToHighScoreFragment(score: Int) {
        highScoreFragment.addScore(score)
    }
}