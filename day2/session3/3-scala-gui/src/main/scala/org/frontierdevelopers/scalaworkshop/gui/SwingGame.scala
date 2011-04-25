package org.frontierdevelopers.scalaworkshop.gui

import java.awt.{Dimension, Graphics2D}

import scala.swing.{Component, MainFrame, SimpleSwingApplication}
import swing.event._
import swing.event.Key._
import scala.swing.event.Key.Value

object SwingGame extends SimpleSwingApplication {
  def top = new MainFrame {
    contents = new Component {
      private var x = 100
      private var y = 100

      override protected def paintComponent(g: Graphics2D) {
        g.drawString("*o*", x, y)
      }

      preferredSize = new Dimension(800, 600)
      focusable = true
      listenTo(keys)
      reactions += {
        case KeyPressed(_, key, _, _) => handleKeyPress(key)
      }

      def handleKeyPress(key: Value) {
        key match {
          case Left => x = x - 10
          case Right => x = x + 10
          case Up => y = y - 10
          case Down => y = y + 10
        }
        repaint()
      }
    }
  }
}