package com.example.hw1_obstacleracinggame.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw1_obstacleracinggame.R
import com.example.hw1_obstacleracinggame.databinding.ItemHighScoreBinding
import com.example.hw1_obstacleracinggame.interfaces.callbake_HighScoreItemClicked
import com.example.hw1_obstacleracinggame.models.Score


class HighScoresAdapter(private val score: MutableList<Score>) : RecyclerView.Adapter<HighScoresAdapter.ScoreViewHolder>() {

    var itemCallback: callbake_HighScoreItemClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = ItemHighScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScoreViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return score.size
    }

    private fun getItem(position: Int): Score {
        return if (position in score.indices) score[position] else throw IndexOutOfBoundsException("Invalid position: $position")
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                binding.itemScore.text = holder.itemView.context.getString(R.string.score_text, this.score)
                binding.itemNumber.text = holder.itemView.context.getString(R.string.line_number, position + 1)
            }
        }
    }

    fun addScore(newScore: Score) {
        score.add(newScore)
        score.sortByDescending { it.score }
        val newIndex = score.indexOf(newScore)
        notifyItemInserted(newIndex)
    }

    fun getScores(): List<Int> {
        return score.sortedByDescending { it.score }.map { it.score }
    }

    inner class ScoreViewHolder(val binding: ItemHighScoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemCV.setOnClickListener {
                val item = getItem(adapterPosition)
                itemCallback?.highScoreItemClicked(item.score)
            }
        }
    }
}

