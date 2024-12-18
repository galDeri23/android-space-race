package com.example.hw1_obstacleracinggame

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import com.example.hw1_obstacleracinggame.utilites.Constants
import com.example.hw1_obstacleracinggame.utilites.GameManager
import com.example.hw1_obstacleracinggame.utilites.SignalManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_right_arrow: FloatingActionButton
    private lateinit var main_IMG_left_arrow: FloatingActionButton
    private lateinit var main_LAY_hearts: Array<AppCompatImageView>
    private lateinit var main_LAY_asteroids: Array<Array<AppCompatImageView>>
    private lateinit var main_LAY_space_ships: Array<AppCompatImageView>

    private lateinit var gameManager: GameManager
    private var startTime: Long = 0
    private var timerOn: Boolean = false
    private lateinit var timerJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
        gameManager = GameManager(main_LAY_hearts.size)
        initViews()

        startTimer()
    }

    private fun restartGame() {
        gameManager.resetGame()

        for (heart in main_LAY_hearts) {
            heart.visibility = View.VISIBLE
        }
        updateUI()
        startTimer()
    }

    private fun startTimer() {
        if (!timerOn) {
            startTime = System.currentTimeMillis()
            timerOn = true
            timerJob = lifecycleScope.launch {
                while (timerOn) {
                    delay(Constants.Timer.DELAY)
                    moveDownAstroids()
                    updateAsteroids()
                    if (gameManager.isGameOver) {
                        stopTimer()
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        timerOn = false
        timerJob.cancel()

    }


    private fun initViews() {
        main_IMG_right_arrow.setOnClickListener({ v -> moveRight() })
        main_IMG_left_arrow.setOnClickListener({ v -> moveLeft() })
        gameManager.randomizeAsteroids()
        updateUI()

    }

    private fun toastAndVibrate(mesg: String) {
        SignalManager.getInstance().toast(mesg)
        SignalManager.getInstance().vibrate()
    }

    private fun moveLeft() {
        gameManager.moveLeft()
        updateUI()
    }

    private fun moveRight() {
        gameManager.moveRight()
        updateUI()
    }


    private fun updateUI() {
        updateShip()
        updateAsteroids()

    }

    private fun checkCollision() {
        if (gameManager.checkCollision()) {
            gameManager.hitsAsteroid()
            main_LAY_hearts[main_LAY_hearts.size - gameManager.timesHits].visibility =
                View.INVISIBLE
            toastAndVibrate("You hit an asteroid!")
            if (gameManager.isGameOver) {
                stopTimer()
                toastAndVibrate("Game Over! Try again.")
                restartGame()
            }
        }
    }

    private fun updateHearts() {
        for (i in main_LAY_hearts.indices) {
            main_LAY_hearts[i].visibility = View.VISIBLE
        }
    }

    private fun moveDownAstroids() {
        gameManager.moveAsteroids()
        updateAsteroids()
        gameManager.randomizeAsteroids()
        checkCollision()
    }

    private fun updateAsteroids() {
        for (i in main_LAY_asteroids.indices) {
            for (j in main_LAY_asteroids[i].indices) {
                if (gameManager.asteroidMatrix[i][j]) {
                    main_LAY_asteroids[i][j].visibility = View.VISIBLE
                } else {
                    main_LAY_asteroids[i][j].visibility = View.INVISIBLE
                }
            }
        }

    }

    private fun updateShip() {
        for (i in main_LAY_space_ships.indices) {
            if (i == gameManager.shipIndex) {
                main_LAY_space_ships[i].visibility = View.VISIBLE
            } else {
                main_LAY_space_ships[i].visibility = View.INVISIBLE
            }
        }
    }


    private fun findViews() {
        main_IMG_right_arrow = findViewById(R.id.main_IMG_right_arrow)
        main_IMG_left_arrow = findViewById(R.id.main_IMG_left_arrow)
        main_LAY_hearts = arrayOf(
            findViewById(R.id.main_IMG_hart1),
            findViewById(R.id.main_IMG_hart2),
            findViewById(R.id.main_IMG_hart3)
        )
        main_LAY_asteroids = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_asteroid00),
                findViewById(R.id.main_IMG_asteroid01),
                findViewById(R.id.main_IMG_asteroid02)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid10),
                findViewById(R.id.main_IMG_asteroid11),
                findViewById(R.id.main_IMG_asteroid12)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid20),
                findViewById(R.id.main_IMG_asteroid21),
                findViewById(R.id.main_IMG_asteroid22)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid30),
                findViewById(R.id.main_IMG_asteroid31),
                findViewById(R.id.main_IMG_asteroid32)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid40),
                findViewById(R.id.main_IMG_asteroid41),
                findViewById(R.id.main_IMG_asteroid42)
            )
        )
        main_LAY_space_ships = arrayOf(
            findViewById(R.id.main_IMG_space_ship1),
            findViewById(R.id.main_IMG_space_ship2),
            findViewById(R.id.main_IMG_space_ship3)
        )

    }
}