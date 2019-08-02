package game_ui.game_2048

import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
import javafx.animation.TranslateTransition
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.util.Duration

class AnimatedScore(text: String, animationTime: Double, parent: Pane) : Label(text) {
    init {
        parent.children.add(this)

        styleClass.setAll("score-increase")
        val transition = TranslateTransition(Duration.millis(animationTime))
        transition.byY = -50.0

        val fade = FadeTransition(Duration.millis(animationTime))
        fade.fromValue = 0.70
        fade.toValue = 0.05

        val fullAnimation = ParallelTransition(this, transition, fade)
        fullAnimation.setOnFinished({ parent.children.remove(this) })
        fullAnimation.play()
    }
}