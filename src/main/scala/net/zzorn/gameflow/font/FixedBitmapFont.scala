package net.zzorn.gameflow.font

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import net.zzorn.utils.Area

/**
 *
 */
class FixedBitmapFont(fontBitmap: BufferedImage, charWidth: Int, charHeight: Int, charOrder: String, alternateCharacters: String = null,  unknownIndex: Int = 0) extends BitmapFont {

  private var charLookup: Map[Char, Area] = Map()
  private var unknownCharArea: Area = null

  setupLookup()

  private def setupLookup() {
    unknownCharArea = getCharArea(unknownIndex)
    var i = 0
    while (i < charOrder.length()) {
      val area: Area = getCharArea(i)

      if (alternateCharacters != null && alternateCharacters.length() > i) {
        charLookup += alternateCharacters(i) -> area
      }

      charLookup += charOrder(i) -> area

      i += 1
    }
  }

  private def getCharArea(index: Int): Area = {
    val charsPerRow = fontBitmap.getWidth / charWidth
    val rows = fontBitmap.getHeight / charHeight
    val maxNumChars = charsPerRow * rows

    if (index < 0 || index >= maxNumChars) unknownCharArea
    else {
      val row = index / charsPerRow
      val column = index % charsPerRow
      new Area(charWidth * column, charHeight * row, charWidth * (column + 1), charHeight * (row + 1))
    }
  }

  def textWidth(text: String, scale: Double = 1.0): Int = {
    text.lines.map(_.length()).max * ((charWidth * scale).toInt)
  }

  def textHeight(text: String, scale: Double): Int = {
    (text.count(_ == '\n') + 1) * ((charHeight * scale).toInt)
  }

  def drawText(g: Graphics2D, text: String, x: Double, y: Double, xAlign: Double=0, yAlign: Double=0, scale: Double=1) {
    val ch = (charHeight * scale).toInt
    val cw = (charWidth * scale).toInt
    val startX = (x - xAlign * textWidth(text, scale)).toInt
    val startY = (y - yAlign * textHeight(text, scale)).toInt

    var i = 0;
    var xp = startX
    var yp = startY
    while (i < text.length()) {
      val char = text(i)
      if (char == '\n') {
        // Newline
        xp = startX
        yp += ch
      }
      else {
        // Normal char
        val area = charLookup.getOrElse(char, unknownCharArea)

        // Draw it, scaling as needed
        g.drawImage(fontBitmap, xp, yp, xp+cw, yp+ch, area.x1, area.y1, area.x2, area.y2, null)

        // Move forward on row
        xp += cw
      }

      i += 1
    }
  }
}
