package net.zzorn.gameflow.picture

import java.awt.Graphics2D

/**
 * Something that can be rendered to a graphics.
 */
trait Picture {

  def w: Int
  def h: Int

  def draw(g: Graphics2D, x: Int, y: Int)

  def drawCentered(g: Graphics2D, x: Int, y: Int, scale: Double = 1.0) {
    draw(g, x - w/2, y - h/2)

    // TODO: Scale draw
  }


}