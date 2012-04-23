package net.zzorn.gameflow.picture

import java.awt.Graphics2D

/**
 *
 */
case class ScaledPicture(sourcePic: Picture, scale: Double) extends Picture {
  def w = sourcePic.w * scale
  def h = sourcePic.h * scale

  override def draw(g: Graphics2D, x: Double, y: Double) {
    sourcePic.draw(g, x, y, scale, false)
  }

  def draw(g: Graphics2D, x: Double, y: Double, scaling: Double, center: Boolean) {
    sourcePic.draw(g, x, y, scale*scaling, center)
  }

  def getImage = sourcePic.getImage
}