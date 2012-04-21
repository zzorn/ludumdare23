package net.zzorn.gameflow.picture

import java.awt.Graphics2D

/**
 * Something that can be rendered to a graphics.
 */
trait Picture {

  def w: Int
  def h: Int

  def draw(g: Graphics2D, x: Int, y: Int)

}