package net.zzorn.gameflow.picture

import java.awt.{Transparency, GraphicsEnvironment, Graphics2D, Image}
import java.awt.image.BufferedImage


/**
 * Picture that renders a specified buffered image.
 */
class BufferedImagePicture(image: BufferedImage) extends Picture {

  def w = image.getWidth
  def h = image.getHeight

  def draw(g: Graphics2D, x: Int, y: Int) {
    g.drawImage(image, x, y, null)
  }
}