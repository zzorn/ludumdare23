package net.zzorn.gameflow

import camera.Camera
import font.BitmapFont
import java.awt.Graphics2D

/**
 *
 */
class TextScreenFacet(var textToShow: String, var bitmapFont: BitmapFont, var scale: Double = 1.0) extends BaseFacet {

  private var _visible: Boolean = true

  def isVisible = _visible

  def setVisible(visible: Boolean) {
    _visible = visible;
  }

  def setText(text: String) {
    textToShow = text
  }

  override def render(g: Graphics2D, camera: Camera) {
    if (isVisible) {
      bitmapFont.drawText(g, textToShow, camera.screenCenterX, camera.screenCenterY, 0.5, 0.5, scale)
    }
  }


}
