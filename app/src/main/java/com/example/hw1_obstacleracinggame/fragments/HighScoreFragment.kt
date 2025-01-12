package com.example.hw1_obstacleracinggame.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hw1_obstacleracinggame.MenuActivity
import com.example.hw1_obstacleracinggame.R
import com.example.hw1_obstacleracinggame.adapters.HighScoresAdapter
import com.example.hw1_obstacleracinggame.interfaces.callbake_HighScoreItemClicked
import com.example.hw1_obstacleracinggame.models.Score
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton


class HighScoreFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var highScoresAdapter: HighScoresAdapter
    private lateinit var highScoresMenuButton: MaterialButton

    var highScoreItemClicked: callbake_HighScoreItemClicked? = null
    private var onDataReadyListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_high_score, container, false)
        findViews(view)
        initViews()


        loadScoresFromSharedPreferences()

        return view
    }

    private fun initViews() {
        highScoresMenuButton.setOnClickListener {
            requireActivity().finish()
        }

        highScoresAdapter = HighScoresAdapter(mutableListOf())
        highScoresAdapter.itemCallback = object : callbake_HighScoreItemClicked {
            override fun highScoreItemClicked(score: Int, lat: Double, lon: Double) {
                highScoreItemClicked?.highScoreItemClicked(score, lat, lon)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = highScoresAdapter
    }

    private fun findViews(view: View) {
        recyclerView = view.findViewById(R.id.highScores_RecyclerView)
        highScoresMenuButton = view.findViewById(R.id.highScores_menu)
    }

    fun getAllScoresWithLocations(): List<Pair<String, LatLng>> {
        return highScoresAdapter.getScores().map { score ->
            Pair("Score: ${score.score}", LatLng(score.lat, score.lon))
        }
    }

    fun setOnDataReadyListener(listener: () -> Unit) {
        onDataReadyListener = listener
    }

    private fun saveScoresToSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("HighScorePrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the list of scores to JSON
        val scoresJson = highScoresAdapter.getScores().joinToString(separator = ";") { score ->
            "${score.score},${score.lat},${score.lon}"
        }
        editor.putString("scores", scoresJson)
        editor.apply()

        Log.d("HighScoreFragment", "Scores saved: $scoresJson")
    }

    private fun loadScoresFromSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("HighScorePrefs", MODE_PRIVATE)
        val scoresJson = sharedPreferences.getString("scores", "")

        val scores = scoresJson?.split(";")?.mapNotNull { entry ->
            val parts = entry.split(",")
            if (parts.size == 3) {
                val score = parts[0].toIntOrNull()
                val lat = parts[1].toDoubleOrNull()
                val lon = parts[2].toDoubleOrNull()
                if (score != null && lat != null && lon != null) {
                    Score.Builder()
                        .score(score)
                        .latitude(lat)
                        .longitude(lon)
                        .build()
                } else null
            } else null
        } ?: emptyList()

        highScoresAdapter.updateScores(scores)
        onDataReadyListener?.invoke()
    }

    fun addScore(score: Int, lat: Double, lon: Double) {
        val newScore = Score.Builder()
            .score(score)
            .latitude(lat)
            .longitude(lon)
            .build()

        highScoresAdapter.addScore(newScore)
        saveScoresToSharedPreferences()
    }
}