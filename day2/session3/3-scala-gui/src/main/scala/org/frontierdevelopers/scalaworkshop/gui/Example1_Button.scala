package org.frontierdevelopers.scalaworkshop.gui

import swing.{Button, MainFrame, SimpleSwingApplication}

object Example1_Button extends SimpleSwingApplication {
  def top = new MainFrame {
    contents = new Button("Hello")
  }
}