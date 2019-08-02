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
import javafx.scene.layout.VBox
import tornadofx.*

/**
 * The top of the ui, containing information and control.
 */
class Top(
    private val margin: Double,
    private val gameRestart: () -> Unit
) : VBox() {
    private val scoreProperty = SimpleIntegerProperty()
    private val highScoreProperty = SimpleIntegerProperty()

    init {
        padding = Insets(
            margin / 2.0, margin / 2.0, margin / 2.0, margin / 2.0
        )
        add(

            borderpane {
                left = label(Style.TITLE).addClass(Style.gameTitle)
                right = borderpane {
                    padding = Insets(margin / 2.0, 0.0, margin / 2.0, 0.0)
                    left = scoreRect(
                        Style.SCORE_RECT_TITLE, scoreProperty, margin
                    )
                    right = scoreRect(
                        Style.HIGH_SCORE_RECT_TITLE, highScoreProperty, margin
                    )

                    // Some empty space, because that's the easiest way I could put
                    //  some space between the rectangles.
                    center = rectangle(margin, margin, margin, margin) {
                        fill = c("#0000")  // TODO: Add to Style class
                    }
                }
            }
        )
        add(
            borderpane {
                padding = Insets(margin, 0.0, margin, 0.0)
                left = borderpane {// TODO: Is this necessary?
                    center = label(Style.INSTRUCTION_TEXT).addClass(Style.instruction)
                }
                right = button(Style.NEW_GAME_BUTTON_TEXT) {
                    addClass(Style.newGameButton)
                    padding = Insets(margin)
                    // Don't put the raw game. Use the GUI version of it because
                    //  otherwise there won't be a GUI-update
                    setOnAction { gameRestart() }
                }
            }
        )
    }

    private fun Node.scoreRect(title: String, score: Property<Number>, margin: Double) =
        vbox {
            padding = Insets(margin / 2.0, margin, margin / 2.0, margin)
            alignment = Pos.CENTER

            label(title).addClass(Style.scoreLabel)
            label(score).addClass(Style.scoreValue)
        }.addClass(Style.scoreTile)
}