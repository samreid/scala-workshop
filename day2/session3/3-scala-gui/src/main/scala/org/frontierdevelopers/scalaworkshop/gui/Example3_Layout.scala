package org.frontierdevelopers.scalaworkshop.gui

import swing._

object Example3_Layout extends SimpleSwingApplication {
  def top = new MainFrame {
    contents = new BoxPanel(Orientation.Vertical) {
      contents += new Label {text = "Hello"}
      contents += new Button {text = "A button"}
      contents += new CheckBox {text = "A checkbox"}
      contents += new RadioButton {text = "A radio button"}
      contents += new Slider
      contents += new TextField{text = "a text field"}
    }
  }
}