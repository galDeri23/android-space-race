package com.example.hw1_obstacleracinggame

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import com.example.hw1_obstacleracinggame.interfaces.TiltCallback
import com.example.hw1_obstacleracinggame.utilites.Constants
import com.example.hw1_obstacleracinggame.utilites.GameManager
import com.example.hw1_obstacleracinggame.utilites.SignalManager
import com.example.hw1_obstacleracinggame.utilites.SingleSoundPlayer
import com.example.hw1_obstacleracinggame.utilites.TiltDetector
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_right_arrow: FloatingActionButton
    private lateinit var main_IMG_left_arrow: FloatingActionButton
    private lateinit var main_LAY_hearts: Array<AppCompatImageView>
    private lateinit var main_LAY_asteroids: Array<Array<AppCompatImageView>>
    private lateinit var main_IMG_currency00 : Array<Array<AppCompatImageView>>
    private lateinit var main_LAY_space_ships: Array<AppCompatImageView>
    private lateinit var main_LBL_score: MaterialTextView
    private lateinit var tiltDetector: TiltDetector
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
        val isSensorEnabled = intent.extras?.getBoolean("SensorEnabled", false) ?: false
        val isFastSpeed = intent.extras?.getBoolean("FastSpeed", false) ?: false

        // Use these preferences to configure the game
        if (isSensorEnabled) {
            initTiltDetector()
            main_IMG_right_arrow.visibility = View.INVISIBLE
            main_IMG_left_arrow.visibility = View.INVISIBLE
        }
        if (isFastSpeed) {
            startTimer(Constants.Timer.FAST)
        }

        startTimer(Constants.Timer.DELAY)
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            context = this,
            tiltCallback = object : TiltCallback {
                override fun tiltX() {
                    if (tiltDetector.tiltCounterX > 0) {
                        gameManager.moveRight()
                    } else {
                        gameManager.moveLeft()
                    }
                }
            }
        )
        tiltDetector.start()
    }

//    private fun restartGame() {
//        gameManager.resetGame()
//
//        for (heart in main_LAY_hearts) {
//            heart.visibility = View.VISIBLE
//        }
//        updateUI()
//        startTimer()
//    }

    private fun startTimer(speed: Long ) {
        timerOn = true
        timerJob = lifecycleScope.launch {
            while (timerOn) {
                delay(speed)
                moveDownAsteroidsAndCurrency()
                updateAsteroids()
                updateCurrency()
                if (gameManager.isGameOver) {
                    stopTimer()
                }
            }
        }
    }

    private fun stopTimer() {
        timerOn = false
        timerJob.cancel()

    }


    private fun initViews() {
        main_LBL_score.text = gameManager.score.toString()
        main_IMG_right_arrow.setOnClickListener({ v -> moveRight() })
        main_IMG_left_arrow.setOnClickListener({ v -> moveLeft() })
        gameManager.randomizeAsteroidsAndCurrency()
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
        updateCurrency()
    }

    private fun checkCollisionAsteroid() {
        if (gameManager.checkCollisionAsteroid()) {
            gameManager.hitsAsteroid()
            main_LAY_hearts[main_LAY_hearts.size - gameManager.timesHits].visibility =
                View.INVISIBLE
            toastAndVibrate("You hit an asteroid!")
            SingleSoundPlayer(this).playSound(R.raw.boom)
            if (gameManager.isGameOver) {
                stopTimer()
                toastAndVibrate("Game Over! Try again.")
                //restartGame()
            }
        }
    }
    private fun checkCollisionCurrency() {
        if (gameManager.checkCollisionCurrency()) {
            gameManager.hitsCurrency()
            main_LBL_score.text = gameManager.score.toString()
            toastAndVibrate("You collected currency!")
        }
    }

    private fun updateHearts() {
        for (i in main_LAY_hearts.indices) {
            main_LAY_hearts[i].visibility = View.VISIBLE
        }
    }

    private fun moveDownAsteroidsAndCurrency() {
        gameManager.moveAsteroids()
        gameManager.moveCurrency()
        updateAsteroids()
        updateCurrency()
        gameManager.randomizeAsteroidsAndCurrency()
        checkCollisionAsteroid()
        checkCollisionCurrency()
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
    private fun updateCurrency() {
        for (i in main_IMG_currency00.indices) {
            for (j in main_IMG_currency00[i].indices) {
                if (gameManager.currencyMatrix[i][j]) {
                    main_IMG_currency00[i][j].visibility = View.VISIBLE
                } else {
                    main_IMG_currency00[i][j].visibility = View.INVISIBLE
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
        main_LBL_score = findViewById(R.id.main_LBL_score)
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
                findViewById(R.id.main_IMG_asteroid02),
                findViewById(R.id.main_IMG_asteroid03),
                findViewById(R.id.main_IMG_asteroid04)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid10),
                findViewById(R.id.main_IMG_asteroid11),
                findViewById(R.id.main_IMG_asteroid12),
                findViewById(R.id.main_IMG_asteroid13),
                findViewById(R.id.main_IMG_asteroid14)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid20),
                findViewById(R.id.main_IMG_asteroid21),
                findViewById(R.id.main_IMG_asteroid22),
                findViewById(R.id.main_IMG_asteroid23),
                findViewById(R.id.main_IMG_asteroid24)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid30),
                findViewById(R.id.main_IMG_asteroid31),
                findViewById(R.id.main_IMG_asteroid32),
                findViewById(R.id.main_IMG_asteroid33),
                findViewById(R.id.main_IMG_asteroid34)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid40),
                findViewById(R.id.main_IMG_asteroid41),
                findViewById(R.id.main_IMG_asteroid42),
                findViewById(R.id.main_IMG_asteroid43),
                findViewById(R.id.main_IMG_asteroid44)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid50),
                findViewById(R.id.main_IMG_asteroid51),
                findViewById(R.id.main_IMG_asteroid52),
                findViewById(R.id.main_IMG_asteroid53),
                findViewById(R.id.main_IMG_asteroid54)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid60),
                findViewById(R.id.main_IMG_asteroid61),
                findViewById(R.id.main_IMG_asteroid62),
                findViewById(R.id.main_IMG_asteroid63),
                findViewById(R.id.main_IMG_asteroid64)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid70),
                findViewById(R.id.main_IMG_asteroid71),
                findViewById(R.id.main_IMG_asteroid72),
                findViewById(R.id.main_IMG_asteroid73),
                findViewById(R.id.main_IMG_asteroid74)
            )

        )
        main_LAY_space_ships = arrayOf(
            findViewById(R.id.main_IMG_space_ship1),
            findViewById(R.id.main_IMG_space_ship2),
            findViewById(R.id.main_IMG_space_ship3),
            findViewById(R.id.main_IMG_space_ship4),
            findViewById(R.id.main_IMG_space_ship5)
        )
        main_IMG_currency00 = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_currency00),
                findViewById(R.id.main_IMG_currency01),
                findViewById(R.id.main_IMG_currency02),
                findViewById(R.id.main_IMG_currency03),
                findViewById(R.id.main_IMG_currency04),
            ),
            arrayOf(
                findViewById(R.id.main_IMG_currency10),
                findViewById(R.id.main_IMG_currency11),
                findViewById(R.id.main_IMG_currency12),
                findViewById(R.id.main_IMG_currency13),
                findViewById(R.id.main_IMG_currency14),
            ),
            arrayOf(
                findViewById(R.id.main_IMG_currency20),
                findViewById(R.id.main_IMG_currency21),
                findViewById(R.id.main_IMG_currency22),
                findViewById(R.id.main_IMG_currency23),
                findViewById(R.id.main_IMG_currency24),
            ),
            arrayOf(
                findViewById(R.id.main_IMG_currency30),
                findViewById(R.id.main_IMG_currency31),
                findViewById(R.id.main_IMG_currency32),
                findViewById(R.id.main_IMG_currency33),
                findViewById(R.id.main_IMG_currency34),
            ),
            arrayOf(
                findViewById(R.id.main_IMG_currency40),
                findViewById(R.id.main_IMG_currency41),
                findViewById(R.id.main_IMG_currency42),
                findViewById(R.id.main_IMG_currency43),
                findViewById(R.id.main_IMG_currency44),
            ),
            arrayOf(
                findViewById(R.id.main_IMG_currency50),
                findViewById(R.id.main_IMG_currency51),
                findViewById(R.id.main_IMG_currency52),
                findViewById(R.id.main_IMG_currency53),
                findViewById(R.id.main_IMG_currency54),
            ),
            arrayOf(
                findViewById(R.id.main_IMG_currency60),
                findViewById(R.id.main_IMG_currency61),
                findViewById(R.id.main_IMG_currency62),
                findViewById(R.id.main_IMG_currency63),
                findViewById(R.id.main_IMG_currency64),
            ),
            arrayOf(
                findViewById(R.id.main_IMG_currency70),
                findViewById(R.id.main_IMG_currency71),
                findViewById(R.id.main_IMG_currency72),
                findViewById(R.id.main_IMG_currency73),
                findViewById(R.id.main_IMG_currency74),
            )
        )
    }

}