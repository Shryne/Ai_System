/*
 * Copyright 2019 Shryne
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

package game_ui.game_2048.tile

import game_ui.game_2048.Style
import game_ui.game_2048.Style.Companion.fromLog
import javafx.animation.PauseTransition
import javafx.animation.ScaleTransition
import javafx.animation.TranslateTransition
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.util.Duration
import tornadofx.*

class FXTile(number: Int, val index: Int) : Tile<FXTile> {
    companion object { // debug stuff
        private const val TIME_MULTIPLIER = 1.0

        private const val SPAWN_TIME = 200.0 * TIME_MULTIPLIER
        private const val MOVE_TIME = 120.0 * TIME_MULTIPLIER
        private const val MERGE_TIME = 140.0 * TIME_MULTIPLIER
        const val FULL_MERGE_TIME = MOVE_TIME + MERGE_TIME
        const val MAX_ANIMATION_TIME = SPAWN_TIME + FULL_MERGE_TIME
    }

    var animationSpeed: Double = 1.0
    var futureNumber = number

    var label: Label
    val visualContent = StackPane().apply {
        addClass(Style.tile, Style.tileStyles.fromLog(number))
        label = label {
            text = number.toString()
            addClass(Style.tileText, Style.tileTextStyles.fromLog(number))
        }
        visibleWhen(label.textProperty().isNotEqualTo("0"))
        useMaxSize = true
    }

    override var number = number
        set(value) {
            label.removeClass(Style.tileTextStyles.fromLog(field))
            visualContent.removeClass(Style.tileStyles.fromLog(field))

            field = value
            label.text = value.toString()

            label.addClass(Style.tileText, Style.tileTextStyles.fromLog(value))
            visualContent.addClass(Style.tile, Style.tileStyles.fromLog(value))
        }


    fun reset(newNumber: Int) {
        number = newNumber
        futureNumber = newNumber
    }

    override fun push(to: FXTile, onFinished: () -> Unit) {
        futureNumber = 0
        to.futureNumber = number

        TranslateTransition(Duration.millis(MOVE_TIME / animationSpeed), visualContent).apply {
            val oldLayoutX = to.visualContent.layoutX
            val oldLayoutY = to.visualContent.layoutY
            val oldTranslateX = to.visualContent.translateX
            val oldTranslateY = to.visualContent.translateY

            toX = oldLayoutX + oldTranslateX - visualContent.layoutX
            toY = oldLayoutY - oldTranslateY - visualContent.layoutY
            setOnFinished {
                onFinished()
                visualContent.layoutX = oldLayoutX
                visualContent.layoutY = oldLayoutY
                visualContent.translateX = oldTranslateX
                visualContent.translateY = oldTranslateY

                number = 0
                to.number = to.futureNumber
            }
        }.play()
    }

    override fun merge(to: FXTile) {
        push(to) {
            to.futureNumber = number * 2
            ScaleTransition(Duration.millis(MERGE_TIME / animationSpeed), to.visualContent).apply {
                fromX = 1.3
                fromY = 1.3
                toX = 1.0
                toY = 1.0
            }.play()
        }
    }

    override fun spawn() {
        PauseTransition(
            Duration.millis(FULL_MERGE_TIME / animationSpeed)
        ).apply {
            setOnFinished {
                ScaleTransition(
                    Duration.millis(SPAWN_TIME / animationSpeed),
                    visualContent
                ).apply {
                    fromX = 0.2
                    fromY = 0.2
                    toX = 1.0
                    toY = 1.0
                }.play()
                visualContent.scaleX = 0.2
                visualContent.scaleY = 0.2
                number = futureNumber
            }
        }.play()
    }

    override fun toString() = number.toString()
}