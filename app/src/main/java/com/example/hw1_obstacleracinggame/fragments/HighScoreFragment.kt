package com.example.hw1_obstacleracinggame.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
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


        val score = arguments?.getInt("EXTRA_SCORE", 0) ?: 0
        val lat = arguments?.getDouble("EXTRA_LAT")
        val lon = arguments?.getDouble("EXTRA_LON")

        if (score > 0 && lat != null && lon != null) {
            addScore(score, lat, lon)
        }

        return view
    }

    private fun initViews() {
        highScoresMenuButton.setOnClickListener {
            val intent = Intent(requireContext(), MenuActivity::class.java)
            startActivity(intent)
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


        val scoresJson = highScoresAdapter.getScores()
            .sortedByDescending { it.score }
            .take(10)
            .joinToString(";") { score ->
                "${score.score},${score.lat},${score.lon}"
            }
        editor.putString("scores", scoresJson)
        editor.apply()
    }

    private fun loadScoresFromSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("HighScorePrefs", MODE_PRIVATE)
        val scoresJson = sharedPreferences.getString("scores", "") ?: ""
        val scores = scoresJson.split(";").mapNotNull { entry ->
            val parts = entry.split(",")
            if (parts.size == 3) {
                val score = parts[0].toIntOrNull()
                val lat = parts[1].toDoubleOrNull()
                val lon = parts[2].toDoubleOrNull()
                if (score != null && lat != null && lon != null) {
                    Score.Builder().score(score).latitude(lat).longitude(lon).build()
                } else null
            } else null
        }


        highScoresAdapter.updateScores(scores)
        onDataReadyListener?.invoke()
    }

    fun addScore(score: Int, lat: Double, lon: Double) {
        val newScore = Score.Builder().score(score).latitude(lat).longitude(lon).build()


        if (highScoresAdapter.getScores().any { it.score == score && it.lat == lat && it.lon == lon }) {
            Log.d("HighScoreFragment", "Score already exists, not adding again.")
            return
        }

        highScoresAdapter.addScore(newScore)
        saveScoresToSharedPreferences()
    }
}
