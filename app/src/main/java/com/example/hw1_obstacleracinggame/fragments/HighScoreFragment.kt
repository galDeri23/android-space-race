package com.example.hw1_obstacleracinggame.fragments

import android.content.Context.MODE_PRIVATE
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
import com.example.hw1_obstacleracinggame.models.Score

class HighScoreFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var highScoresAdapter: HighScoresAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_high_score, container, false)
        findViews(view)
        initViews()

        loadScoresFromSharedPreferences()

        // add score after fragment is created
        val score = arguments?.getInt("EXTRA_SCORE", 0) ?: 0
        if (score > 0) {
            Log.d("HighScoreFragment", "Adding score from arguments: $score")
            addScore(score)
        }

        return view
    }

    private fun initViews() {
        highScoresAdapter = HighScoresAdapter(mutableListOf())
        recyclerView.adapter = highScoresAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        Log.d("HighScoreFragment", "RecyclerView initialized with empty adapter")
    }

    private fun findViews(view: View) {
        recyclerView = view.findViewById(R.id.highScores_RecyclerView)
    }

    fun addScore(score: Int) {
        Log.d("HighScoreFragment", "Adding score: $score to adapter")
        if (!::highScoresAdapter.isInitialized) {
            Log.e("HighScoreFragment", "Adapter is not initialized!")
            return
        }
        val newScore = Score.Builder()
            .score(score)
            .build()
        highScoresAdapter.addScore(newScore)
        saveScoresToSharedPreferences()
    }
    private fun saveScoresToSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("HighScorePrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert scores to JSON
        val scoresJson = highScoresAdapter.getScores().joinToString(separator = ",")
        editor.putString("scores", scoresJson)
        editor.apply()

        Log.d("HighScoreFragment", "Scores saved: $scoresJson")
    }

    private fun loadScoresFromSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("HighScorePrefs", MODE_PRIVATE)
        val scoresJson = sharedPreferences.getString("scores", "")

        if (!scoresJson.isNullOrEmpty()) {
             // Convert JSON to scores
            val scoresList = scoresJson.split(",").map { it.toInt() }
            for (score in scoresList) {
                highScoresAdapter.addScore(Score.Builder().score(score).build())
            }
            Log.d("HighScoreFragment", "Scores loaded: $scoresList")
        } else {
            Log.d("HighScoreFragment", "No scores found in SharedPreferences")
        }
    }


}
