package kotlinwonders.ui

import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.stage.Stage

fun showButton(text: String, buttonText: String, callback: () -> Unit) {
    val root = HBox()
    root.spacing = 10.0
    val label = Label(text)
    val button = Button(buttonText).apply {
        setOnAction {
            callback()
            (it.source as Node).scene.window.hide()
        }
    }
    root.children.addAll(label, button)
    Stage().apply { scene = Scene(root, 500.0, 40.0) }.show()
}

fun showNothing() {
    val root = HBox().apply {
        spacing = 10.0
        children.addAll()
    }
    Stage().apply { scene = Scene(root, 30.0, 10.0) }.show()
}