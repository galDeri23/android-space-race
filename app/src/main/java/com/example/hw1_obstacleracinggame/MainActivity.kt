package com.example.hw1_obstacleracinggame

import android.opengl.Matrix
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_right_arrow:FloatingActionButton
    private lateinit var main_IMG_left_arrow: FloatingActionButton
    private lateinit var main_LAY_hearts: Array<AppCompatImageView>
    private lateinit var main_LAY_asteroids: Array<Array<AppCompatImageView>>
    private lateinit var main_LAY_space_ships: Array<AppCompatImageView>

    private lateinit var  gameManager: GameManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
        gameManager = GameManager(main_LAY_hearts.size)
        initViews()


    }

    private fun initViews() {
        main_IMG_right_arrow.setOnClickListener({ v-> moveRight() })
        main_IMG_left_arrow.setOnClickListener({v -> moveLeft() })
        updateUI()

    }

    private fun moveLeft() {
        gameManager.moveLeft()
        updateUI()
    }



    private fun updateUI() {
        updateShip()
    }

    private fun updateShip() {
        for(i in main_LAY_space_ships.indices){
            if(i == gameManager.shipIndex){
                main_LAY_space_ships[i].visibility = View.VISIBLE
            }else{
                main_LAY_space_ships[i].visibility = View.INVISIBLE
            }
        }
    }

    private fun moveRight() {
        gameManager.moveRight()
        updateUI()
    }


    private fun findViews() {
        main_IMG_right_arrow = findViewById(R.id.main_IMG_right_arrow)
        main_IMG_left_arrow = findViewById(R.id.main_IMG_left_arrow)
        main_LAY_hearts = arrayOf(findViewById(R.id.main_IMG_hart1),
                                  findViewById(R.id.main_IMG_hart2),
                                  findViewById(R.id.main_IMG_hart3))
        main_LAY_asteroids = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_asteroid00),
                findViewById(R.id.main_IMG_asteroid01),
                findViewById(R.id.main_IMG_asteroid02)),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid10),
                findViewById(R.id.main_IMG_asteroid11),
                findViewById(R.id.main_IMG_asteroid12)),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid20),
                findViewById(R.id.main_IMG_asteroid21),
                findViewById(R.id.main_IMG_asteroid22)),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid30),
                findViewById(R.id.main_IMG_asteroid31),
                findViewById(R.id.main_IMG_asteroid32)),
            arrayOf(
                findViewById(R.id.main_IMG_asteroid40),
                findViewById(R.id.main_IMG_asteroid41),
                findViewById(R.id.main_IMG_asteroid42)))
        main_LAY_space_ships = arrayOf(findViewById(R.id.main_IMG_space_ship1),
                                       findViewById(R.id.main_IMG_space_ship2),
                                       findViewById(R.id.main_IMG_space_ship3))

    }
}