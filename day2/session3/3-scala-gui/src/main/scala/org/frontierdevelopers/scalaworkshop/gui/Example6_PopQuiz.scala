package org.frontierdevelopers.scalaworkshop.gui

import swing._
import event.ActionEvent

object Example6_PopQuiz extends SimpleSwingApplication {
  def top = new MainFrame {
    contents = new BoxPanel(Orientation.Vertical) {
      val checkBox = new CheckBox {text = "A checkbox"}
      contents += checkBox
      val radioButton = new RadioButton {text = "Radio button!"; selected = true}
      contents += radioButton
      listenTo(checkBox)
      reactions += {
        case ActionEvent(s) => radioButton.selected = !checkBox.selected
      }
    }
  }
}