package net.zzorn.gameflow.picture

import java.awt.{Graphics2D, Image}
import java.awt.image.BufferedImage


/**
 *
 */
class PartPicture(image: BufferedImage, x: Int, y: Int, picW: Int, picH: Int) extends Picture {

  def w: Double = picW
  def h: Double = picH


  def draw(g: Graphics2D, x: Double, y: Double, scale: Double, center: Boolean) {
    val targetW = picW * scale
    val targetH = picH * scale
    val targetX = if (center) x - targetW/2 else x
    val targetY = if (center) y - targetH/2 else y

    g.drawImage(
      image,
      targetX.toInt,
      targetY.toInt,
      targetW.toInt,
      targetH.toInt,
      this.x,
      this.y,
      this.x + picW,
      this.y + picH,
      null)

  }

  def getImage: Image = {
    image.getSubimage(x, y, picW, picH)
  }
}
