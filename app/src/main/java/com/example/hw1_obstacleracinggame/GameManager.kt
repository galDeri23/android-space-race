package com.example.hw1_obstacleracinggame

import android.view.View
import androidx.appcompat.widget.AppCompatImageView

class GameManager(private val lifeCount: Int = 3, private val numLanes: Int = 3, private val numRows: Int = 5) {

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


//    private fun moveLeft(array: Array<AppCompatImageView>) {
//        for (i in 0 until array.size){
//            if(array[i].visibility == View.VISIBLE){
//                array[i].visibility = View.INVISIBLE
//                if(i != 0){
//                    array[i - 1].visibility = View.VISIBLE
//                }
//                break
//            }
//        }
//    }
//
//    private fun moveRight() {
//        for (i in 0 until main_LAY_space_ships.size){
//            if(main_LAY_space_ships[i].visibility == View.VISIBLE){
//                main_LAY_space_ships[i].visibility = View.INVISIBLE
//                if(i != main_LAY_space_ships.size - 1){
//                    main_LAY_space_ships[i + 1].visibility = View.VISIBLE
//                }
//                break
//            }
//        }
//    }









}