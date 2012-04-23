package net.zzorn.gameflow.picture

import java.awt.{Transparency, GraphicsEnvironment, Graphics2D, Image}
import java.awt.image.BufferedImage


/**
 * Picture that renders a specified buffered image.
 */
class BufferedImagePicture(image: BufferedImage) extends Picture {

  def w = image.getWidth
  def h = image.getHeight

  override def draw(g: Graphics2D, x: Double, y: Double) {
    g.drawImage(image, x.toInt, y.toInt, null)
  }


  def draw(g: Graphics2D, x: Double, y: Double, scale: Double, center: Boolean) {
    val targetW = w * scale
    val targetH = h * scale
    val targetX = if (center) x - targetW/2 else x
    val targetY = if (center) y - targetH/2 else y

    g.drawImage(
      image,
      targetX.toInt,
      targetY.toInt,
      targetW.toInt,
      targetH.toInt,
      null)
  }

  def getImage = image
}