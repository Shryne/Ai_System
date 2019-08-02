package game_ui.game_2048

import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.FontWeight
import javafx.stage.Screen
import tornadofx.*

/**
 * This class is an alternative to the common javaFx css files and it contains
 * the sizes, colors, fonts, texts and other styling stuff for the 2048 game.
 */
class Style : Stylesheet() {
    companion object {
        /* resolution */
        const val WIDTH_PERCENTAGE = 0.4
        const val HEIGHT_PERCENTAGE = 0.5
        const val GRID_SIZE_PERCENTAGE = 0.6
        val dpi = Screen.getPrimary().dpi
        val scaleX = Screen.getPrimary().outputScaleX
        val scaleY = Screen.getPrimary().outputScaleY

        /* texts */
        const val TITLE = "2048"
        const val SCORE_RECT_TITLE = "SCORE"
        const val HIGH_SCORE_RECT_TITLE = "BEST"
        const val INSTRUCTION_TEXT = "Join equal tiles to get 2048!"
        const val NEW_GAME_BUTTON_TEXT = "Restart"

        val game2048 by cssclass()

        val root by cssclass()

        /* top */
        val gameTitle by cssclass()

        val scoreLabel by cssclass()
        val scoreValue by cssclass()
        val scoreIncrease by cssclass()
        val scoreTile by cssclass()

        val instruction by cssclass()
        val newGameButton by cssclass()

        /* game area */
        val grid by cssclass()
        val tile by cssclass()
        val tileText by cssclass()

        val tile0 by cssclass()
        val text0 by cssclass()

        val tile2 by cssclass()
        val text2 by cssclass()

        val tile4 by cssclass()
        val text4 by cssclass()

        val tile8 by cssclass()
        val text8 by cssclass()

        val tile16 by cssclass()
        val text16 by cssclass()

        val tile32 by cssclass()
        val text32 by cssclass()

        val tile64 by cssclass()
        val text64 by cssclass()

        val tile128 by cssclass()
        val text128 by cssclass()

        val tile256 by cssclass()
        val text256 by cssclass()

        val tile512 by cssclass()
        val text512 by cssclass()

        val tile1024 by cssclass()
        val text1024 by cssclass()

        val tile2048 by cssclass()
        val text2048 by cssclass()

        val tile8096 by cssclass()
        val text8096 by cssclass()

        val tile16182 by cssclass()
        val text16182 by cssclass()

        val tile32364 by cssclass()
        val text32364 by cssclass()

        val tileStyles = arrayOf(
            tile0, tile2, tile4, tile8, tile16, tile32, tile64, tile128,
            tile256, tile512, tile1024, tile2048, tile8096, tile16182,
            tile32364
        )
        val tileTextStyles = arrayOf(
            text0, text2, text4, text8, text16, text32, text64, text128,
            text256, text512, text1024, text2048, text8096, text16182,
            text32364
        )

        /**
         * Made for tileStyles and tileTextStyles to allow the usage with
         * logarithmic values (instead of get(11) one can write fromLog(1024) to
         * get the same value).
         * Source: [http://stackoverflow.com/a/3305710/7563350]
         */
        fun Array<CssRule>.fromLog(bits: Int): CssRule {
            var bits = bits
            var log = 0
            if (bits and 0xffff0000.toInt() != 0) {
                bits = bits ushr 16
                log = 16
            }
            if (bits >= 256) {
                bits = bits ushr 8
                log += 8
            }
            if (bits >= 16) {
                bits = bits ushr 4
                log += 4
            }
            if (bits >= 4) {
                bits = bits ushr 2
                log += 2
            }
            return this[log + bits.ushr(1)]
        }
    }

    init {
        game2048 {
            root {
                backgroundColor += c("#faf8ef")
            }

            /* top */
            gameTitle {
                fontFamily = "Arial"
                fontWeight = FontWeight.BOLD
                fontSize = 55.px
                textFill = c("#776e65")
            }

            scoreLabel {
                fontFamily = "Arial"
                fontWeight = FontWeight.BOLD
                fontSize = 13.px
                textFill = c("#eee4da")
            }

            scoreValue {
                fontFamily = "Arial"
                fontWeight = FontWeight.BOLD
                fontSize = 21.px
                textFill = Color.WHITE
            }

            scoreIncrease {
                alignment = Pos.CENTER
                opacity = 0.7
                fontFamily = "Arial"
                fontSize = 21.px
                textFill = Color.BLACK
                prefWidth = 65.px
            }

            scoreTile {
                backgroundRadius += box(6.px)
                stroke = Paint.valueOf("transparent") // TODO: check whether that line does anything
                backgroundColor += c("#bbada0")
            }

            instruction {
                fontFamily = "Arial"
                fontSize = 18.px
                textFill = c("#776e65")
            }

            newGameButton {
                fontSize = 20.px
                backgroundColor += c("#8f7a66")
                textFill = c("#f9f6f2")
                highlightFill = c("#5a3a27")

                and(Stylesheet.pressed) {
                    backgroundColor += c("#5a3a27")
                }
            }

            /* game area */
            grid {
                val radius = 8.px

                backgroundColor += c("#a39284")
                backgroundRadius += box(radius)
                borderColor += box(Color.BLACK)
                borderWidth += box(1.px)
                borderRadius += box(radius)
            }

            tile {
                backgroundRadius += box(6.px)
                stroke = Paint.valueOf("transparent") // TODO: check whether that line does anything
            }

            tileText {
                fontSize = 55.px
                fontFamily = "Arial"
                fontWeight = FontWeight.BOLD
                stroke = Paint.valueOf("transparent") // TODO: check whether that line does anything
            }

            tile0 { backgroundColor += c("#baaa9c") }
            text0 { textFill = c("#776e65") }

            tile2 { backgroundColor += c("#eee4da") }
            text2 { textFill = c("#776e65") }

            tile4 { backgroundColor += c("#ede0c8") }
            text4 { textFill = c("#776e65") }

            tile8 { backgroundColor += c("#f2b179") }
            text8 { textFill = c("#f9f6f2") }

            tile16 { backgroundColor += c("#f59563") }
            text16 { textFill = c("#f9f6f2") }

            tile32 { backgroundColor += c("#f67c5f") }
            text32 { textFill = c("#f9f6f2") }

            tile64 { backgroundColor += c("#f65e3b") }
            text64 { textFill = c("#f9f6f2") }

            tile128 { backgroundColor += c("#f59563") }
            text128 {
                fontSize = 45.px
                textFill = c("#f9f6f2")
            }

            tile256 { backgroundColor += c("#edcc61") }
            text256 {
                fontSize = 45.px
                textFill = c("#f9f6f2")
            }

            tile512 { backgroundColor += c("#edc850") }
            text512 {
                fontSize = 45.px
                textFill = c("#f9f6f2")
            }

            tile1024 { backgroundColor += c("#edc53f") }
            text1024 {
                fontSize = 35.px
                textFill = c("#f9f6f2")
            }

            tile2048 { backgroundColor += c("#edc22e") }
            text2048 {
                fontSize = 35.px
                textFill = c("#f9f6f2")
            }
        }
    }
}