package org.frontierdevelopers.scalaworkshop.gui

import swing.{Button, MainFrame, SimpleSwingApplication}

object Example1_ButtonInteraction extends SimpleSwingApplication {
  def top = new MainFrame {
    contents = Button("Hello"){
      println("button pressed")
    }
  }
}