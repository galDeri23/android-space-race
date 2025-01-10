package com.example.hw1_obstacleracinggame.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hw1_obstacleracinggame.R
import com.example.hw1_obstacleracinggame.adapters.HighScoresAdapter
import com.example.hw1_obstacleracinggame.interfaces.callbake_HighScoreItemClicked
import com.example.hw1_obstacleracinggame.models.Score

class HighScoreFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var highScoresAdapter: HighScoresAdapter
    var highScoreItemClicked: callbake_HighScoreItemClicked? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_high_score, container, false)
        findViews(view)
        initViews()
        addScore(150)
        addScore(250)
        return view
    }

    fun setCallBack(callback: callbake_HighScoreItemClicked) {
        this.highScoreItemClicked = callback
    }

    private fun initViews() {
        // Initialize the adapter with an empty mutable list
        highScoresAdapter = HighScoresAdapter(mutableListOf())
        recyclerView.adapter = highScoresAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set the callback for item clicks
        highScoresAdapter.itemCallback = object : callbake_HighScoreItemClicked {
            override fun highScoreItemClicked(score: Int) {
                Log.d("HighScoreFragment", "Clicked on score: $score")
                highScoreItemClicked?.highScoreItemClicked(score)
            }
        }
    }

    private fun findViews(view: View) {
        recyclerView = view.findViewById(R.id.highScores_RecyclerView)
    }

    fun addScore(score: Int) {
        val newScore = Score.Builder()
            .score(score)
            .build()
        highScoresAdapter.addScore(newScore)
    }
}