package net.zzorn.gameflow.picture

import java.awt.{Image, Graphics2D}


/**
 * Something that can be rendered to a graphics.
 */
trait Picture {

  def w: Double
  def h: Double

  def draw(g: Graphics2D, x: Double, y: Double) {
    draw(g, x, y, 1.0, false)
  }

  def drawCentered(g: Graphics2D, x: Double, y: Double, scale: Double = 1.0) {
    draw(g, x, y, scale, true)
  }

  def draw(g: Graphics2D, x: Double, y: Double, scale: Double, center: Boolean )

  def getImage: Image
}