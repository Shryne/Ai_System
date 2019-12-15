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

package game_ui.game_2048

import javafx.beans.property.Property
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*
import kotlin.math.max

/**
 * The top of the ui, containing information and control.
 */
class Top(
    private val gameRestart: () -> Unit // TODO: Maybe as an interface?
) : VBox() {
    private val scoreProperty = SimpleIntegerProperty()
    private val highScoreProperty = SimpleIntegerProperty()

    private var scoreRect: Pane

    init {
        addClass(Style.top)
        val title = label(Style.TITLE) {
            addClass(Style.gameTitle)
        }
        scoreRect = scoreRect(
            Style.SCORE_RECT_TITLE, scoreProperty
        )
        val highScoreRect = scoreRect(
            Style.HIGH_SCORE_RECT_TITLE, highScoreProperty
        )
        // the BorderPane is necessary to center the label vertically
        val instruction = borderpane {
            center = label(Style.INSTRUCTION_TEXT).addClass(Style.instruction)
        }
        val restart = button(Style.NEW_GAME_BUTTON_TEXT) {
            addClass(Style.newGameButton)
            // Don't put the raw game. Use the GUI version of it because
            //  otherwise there won't be a GUI-update
            setOnAction { gameRestart() }
        }
        val minWidthListener = ChangeListener<Number> {
            _, _, _ ->
            minWidth =
                max(
                    title.width + scoreRect.width + highScoreRect.width,
                instruction.width + restart.width
            )
        }
        title.widthProperty().addListener(minWidthListener)
        scoreRect.widthProperty().addListener(minWidthListener)
        highScoreRect.widthProperty().addListener(minWidthListener)
        instruction.widthProperty().addListener(minWidthListener)
        restart.widthProperty().addListener(minWidthListener)
        val top = borderpane {
            left = title
            right = hbox {
                padding = Insets(
                    0.0, 0.0, Style.margin.value, 0.0
                )
                spacing = Style.margin.value
                add(scoreRect)
                add(highScoreRect)
            }
        }
        val bottom = borderpane {
            left = instruction
            right = restart
        }
        // necessary because for some reason the top tries to be a little bit
        //  smaller...
        top.minWidthProperty().bind(bottom.widthProperty())
        minHeightProperty().bind(heightProperty())
        add(top)
        add(bottom)
    }

    private fun Pane.scoreRect(title: String, score: Property<Number>) =
        stackpane {
            vbox {
                padding = Style.margin.value.run {
                    Insets(this, this, this, this)
                }
                alignment = Pos.CENTER
                label(title).addClass(Style.scoreLabel)
                label(score).addClass(Style.scoreValue)
            }.addClass(Style.scoreTile)
        }

    fun update(score: Int) {
        if (scoreProperty.value < score) {
            AnimatedScore(
                "+${score - scoreProperty.value}",
                400.0,
                scoreRect
            )
            scoreProperty.value = score
        }
    }
}