package org.frontierdevelopers.scalaworkshop.gui

import swing._
import event.{ValueChanged, ButtonClicked}

object Example4_Events extends SimpleSwingApplication {
  def top = new MainFrame {
    contents = new BoxPanel(Orientation.Vertical) {
      val button = new Button {text = "A button"}
      contents += button
      val slider = new Slider
      contents += slider
      val textField = new TextField {text = "a text field"}
      contents += textField
      listenTo(button)
      listenTo(slider)
      reactions += {
        case ButtonClicked(b) => textField.text = "Button pressed!"
        case ValueChanged(s: Slider) => textField.text = "Slider value: " + s.value
      }
    }
  }
}