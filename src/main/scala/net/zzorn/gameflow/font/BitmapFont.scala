package net.zzorn.gameflow.font

import java.awt.Graphics2D

/**
 *
 */
trait BitmapFont {

  def textWidth(text: String, scale: Double): Int

  def textHeight(text: String, scale: Double): Int

  def drawText(g: Graphics2D, text: String, x: Double, y: Double, xAlign: Double = 0, yAlign: Double = 0, scale: Double = 1)

}
