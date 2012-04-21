package net.zzorn.utils

import java.awt.image.BufferedImage
import java.awt.{Transparency, GraphicsEnvironment, Image}

/**
 *
 */
object ImageUtils {

  def createScreenCompatibleImage(source: Image): BufferedImage = {
    val compatibleImage: BufferedImage = createScreenCompatibleImage(source.getWidth(null), source.getHeight(null))
    compatibleImage.getGraphics.drawImage(source, 0, 0, null)
    compatibleImage
  }

  def createScreenCompatibleImage(w: Int, h: Int, transparency: Int = Transparency.BITMASK): BufferedImage = {
    val graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice.getDefaultConfiguration
    graphicsConfiguration.createCompatibleImage(w, h, transparency)
  }
}