package com.example.hw1_obstacleracinggame.utilites

class GameManager(
    private val lifeCount: Int = 3,
    private val numLanes: Int = 3,
    private val numRows: Int = 5
) {

    var shipIndex: Int = 1
        private set

    var timesHits: Int = 0
        private set

    val asteroidMatrix: Array<Array<Boolean>> = Array(numRows) { Array(numLanes) { false } }

    val isGameOver: Boolean
        get() = timesHits == lifeCount


    fun moveLeft() {
        if (shipIndex > 0) {
            shipIndex--
        }
    }

    fun moveRight() {
        if (shipIndex < numLanes - 1) {
            shipIndex++
        }
    }

    fun hitsAsteroid() {
        timesHits++
    }

    fun resetGame() {
        shipIndex = 1
        timesHits = 0
        asteroidMatrix.forEach { it.fill(false) }
        randomizeAsteroids()
    }

    fun checkCollision(): Boolean {
        return asteroidMatrix[numRows - 1][shipIndex]
    }

    fun moveAsteroids() {
        for (i in numRows - 1 downTo 1) {
            for (j in 0 until numLanes) {
                asteroidMatrix[i][j] = asteroidMatrix[i - 1][j]
            }
        }

        for (j in 0 until numLanes) {
            asteroidMatrix[0][j] = false
        }
    }

    fun randomizeAsteroids() {
        val firstRow = 0
        val randNum = (0..<numLanes).random()
        for (i in 0 until numLanes) {
            asteroidMatrix[firstRow][randNum] = true
        }

    }

}




