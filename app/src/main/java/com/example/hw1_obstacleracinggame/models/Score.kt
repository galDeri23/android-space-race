package com.example.hw1_obstacleracinggame.models

data class Score private constructor(

    val score: Int,
    val lat: Double,
    val lon: Double
){
    class Builder{
        private var score: Int = 0
        private var lat: Double = 0.0
        private var lon: Double = 0.0


        fun score(score: Int) = apply { this.score = score }
        fun latitude(lat: Double) = apply { this.lat = lat }
        fun longitude(lon: Double) = apply { this.lon = lon }

        fun build() = Score(score , lat , lon)
    }
}
