package net.zzorn.gameflow

import camera.Camera
import font.BitmapFont
import java.awt.Graphics2D

/**
 *
 */

class MessageScreenFacet(var bitmapFont: BitmapFont) extends BaseFacet {

  private var _visible: Boolean = true
  private var _title: String = ""
  private var _message: String = ""
  private var showTimeLeft = 0.0
  private var _textScale = 1.0
  private var _titleScale = 4.0

  def isVisible = _visible

  def showMessage(title: String, message: String, showTime: Double = 0, textScale: Double = 1.0, titleScale: Double = 4.0) {
    _title = title
    _message = message
    _textScale = titleScale
    _textScale = textScale
    showTimeLeft = showTime
    _visible = true
  }

  def clearMessage() {
    _visible = false
  }


  override def update(durationSeconds: Double) {
    if (showTimeLeft > 0) {
      showTimeLeft -= durationSeconds
      if (showTimeLeft <= 0) {
        showTimeLeft = 0;
        _visible = false
      }
    }
  }

  override def render(g: Graphics2D, camera: Camera) {
    if (isVisible) {
      val titleH = bitmapFont.textHeight(_title, _titleScale)
      val textH = bitmapFont.textHeight(_message, _textScale)
      val margin = 20
      val titleY = (camera.screenH - titleH - textH - margin) / 2
      bitmapFont.drawText(g, _title, camera.screenCenterX, titleY, 0.5, 0, _titleScale)
      bitmapFont.drawText(g, _message, camera.screenCenterX, titleY + titleH + margin, 0.5, 0, _textScale)
    }
  }


}

