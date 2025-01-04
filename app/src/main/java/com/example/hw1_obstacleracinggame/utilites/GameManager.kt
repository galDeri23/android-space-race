package com.example.hw1_obstacleracinggame.utilites

class GameManager(
    private val lifeCount: Int = 3,
    private val numLanes: Int = 5,
    private val numRows: Int = 8
) {
    var score: Int = 0
        private set

    var shipIndex: Int = 2
        private set

    var timesHits: Int = 0
        private set

    val asteroidMatrix: Array<Array<Boolean>> = Array(numRows) { Array(numLanes) { false } }

    val currencyMatrix: Array<Array<Boolean>> = Array(numRows) { Array(numLanes) { false } }

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
    fun hitsCurrency() {
        score += 10
    }

    fun resetGame() {
        shipIndex = 1
        timesHits = 0
        asteroidMatrix.forEach { it.fill(false) }
        randomizeAsteroidsAndCurrency()
    }

    fun checkCollisionAsteroid(): Boolean {
        return asteroidMatrix[numRows - 1][shipIndex]
    }
    fun checkCollisionCurrency(): Boolean {
        return currencyMatrix[numRows - 1][shipIndex]
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
    fun moveCurrency() {
        for (i in numRows - 1 downTo 1) {
            for (j in 0 until numLanes) {
                currencyMatrix[i][j] = currencyMatrix[i - 1][j]
            }
        }

        for (j in 0 until numLanes) {
            currencyMatrix[0][j] = false
        }
    }

    fun randomizeAsteroidsAndCurrency() {
        val firstRow = 0
        val randNum = (0..<numLanes).random()
        var randNum1 = (0..<numLanes).random()
        while (randNum == randNum1){
             randNum1 = (0..<numLanes).random()
        }
        for (i in 0 until numLanes) {
            asteroidMatrix[firstRow][randNum] = true
            currencyMatrix[firstRow][randNum1] = true
        }
    }

//    fun randomizeCurrency() {
//        val firstRow = 0
//        val randNum = (0..<numLanes).random()
//        for (i in 0 until numLanes) {
//
//            currencyMatrix[firstRow][randNum] = true
//        }
//    }
//    fun checkFirstRowOverlap(): Boolean {
//        val firstRow = 0
//        for (lane in 0 until asteroidMatrix[firstRow].size) {
//            if (asteroidMatrix[firstRow][lane] && currencyMatrix[firstRow][lane]) {
//                return true // Overlap found
//            }
//        }
//        return false // No overlap
//    }
//
//    fun spawnObjects() {
//        randomizeAsteroids()
//        randomizeCurrency()
//
//        while (checkFirstRowOverlap()) {
//            // Re-randomize the currency until there is no overlap
//            randomizeCurrency()
//        }
//    }


}




