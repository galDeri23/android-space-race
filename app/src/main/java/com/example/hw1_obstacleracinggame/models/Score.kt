package com.example.hw1_obstacleracinggame.models

data class Score private constructor(

    val score: Int
){
    class Builder{
        private var score: Int = 0


        fun score(score: Int) = apply { this.score = score }

        fun build() = Score(score)
    }
}
