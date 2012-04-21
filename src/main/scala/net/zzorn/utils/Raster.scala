package net.zzorn.utils

import java.awt.Color
import net.zzorn.utils.ParameterChecker._
import net.zzorn.utils.Area
import java.util.Arrays

object Raster {

  def apply(width: Int, height: Int): Raster = {
    new Raster(new Array[Int](width * height), width, height)
  }
}

/**
 *
 */
// TODO: Support clip area?
case class Raster(data: Array[Int], width: Int, height: Int) {

  private val updatedArea: Area = new Area()
  private val area: Area = new Area(0, 0, width, height)

  /**
   * Clears the whole raster to black.
   */
  def clear() {
    fill(Color.BLACK.getRGB)
  }

  /**
   * Fills the whole raster with the specified color.
   */
  def fill(color: Int) {
    var i = 0
    while (i < data.length) {
      data(i) = color
      i += 1
    }
    updatedArea.includeArea(area)
  }

  /**
   * Sets a pixel to the specified color.  If the pixel is outside the raster bounds, it is not rendered.
   * Also updates the updated area to contain the pixel.
   */
  @inline final def drawPixel(x: Int, y: Int, color: Int) {
    if (x >= 0 && x < width &&
      y >= 0 && y < height) {
      data(x + y * width) = color
      updatedArea.includePoint(x, y)
    }
  }

  /**
   * Sets a pixel to the specified color.  Does no error checking, apart from built-in java array bounds checking.
   * Does not update the updatedArea (that is left for the caller to do).
   */
  @inline final def drawPixelRaw(x: Int, y: Int, color: Int) {
    data(x + y * width) = color
  }

  /**
   * Sets a pixel at the specified data array index to the specified color.
   * Does no error checking, apart from built-in java array bounds checking.
   * Does not update the updatedArea (that is left for the caller to do).
   */
  @inline final def drawPixelRaw(index: Int, color: Int) {
    data(index) = color
  }

  /**
   * Draw the specified source raster to this raster.
   *
   * Only copies the visible area if a part of the area to be copied is outside the bounds of the raster.
   *
   * @param source the source raster to draw.
   * @param destX x position to draw the source raster at.
   * @param destY y position to draw the source raster at.
   */
  @inline final def drawRaster(source: Raster,
                               destX: Int,
                               destY: Int) {
    requireNotNull(source, 'source)
    drawRaster(source, destX, destY, 0, 0, source.width, source.height, false)
  }

  /**
   * Draw the specified source raster to this raster.
   *
   * Only copies the visible area if a part of the area to be copied is outside the bounds of the raster.
   *
   * @param source the source raster to draw.
   * @param destX x position to draw the source raster at.
   * @param destY y position to draw the source raster at.
   * @param alpha if true, performs alpha blending, if false, copies values directly.
   */
  @inline final def drawRaster(source: Raster,
                               destX: Int,
                               destY: Int,
                               alpha: Boolean) {
    requireNotNull(source, 'source)
    drawRaster(source, destX, destY, 0, 0, source.width, source.height, alpha)
  }

  /**
   * Draw a part of the specified source raster to this raster.
   *
   * Only copies the visible area if a part of the area to be copied is outside the bounds of the source or target rasters.
   *
   * @param source the source raster to draw.
   * @param destX x position to draw the source raster at.
   * @param destY y position to draw the source raster at.
   * @param sourceX x position on the source raster to draw from
   * @param sourceY x position on the source raster to draw from
   * @param w number of pixels to copy in x direction.
   * @param h number of pixels to copy in y direction.
   * @param alpha if true, performs alpha blending, if false, copies values directly.
   */
  @inline final def drawRaster(source: Raster,
                               destX: Int,
                               destY: Int,
                               sourceX: Int,
                               sourceY: Int,
                               w: Int,
                               h: Int,
                               alpha: Boolean = false) {
    requireNotNull(source, 'source)

    var dxs = destX
    var dys = destY
    var dxe = destX + w
    var dye = destY + h
    var sxs = sourceX
    var sys = sourceY
    var sxe = sourceX + w
    var sye = sourceY + h

    // Fit destination area to raster bounds
    if (dxs < 0) {
      sxs += -dxs
      dxs = 0
    }
    if (dys < 0) {
      sys += -dys
      dys = 0
    }

    if (dxe > width) {
      sxe -= dxe - width
      dxe = width
    }
    if (dye > height) {
      sye -= dye - height
      dye = height
    }

    // Fit source area to source raster bounds
    if (sxs < 0) {
      dxs += -sxs
      sxs = 0
    }
    if (sys < 0) {
      dys += -sys
      sys = 0
    }

    if (sxe > source.width) {
      dxe -= sxe - source.width
      sxe = source.width
    }
    if (sye > source.height) {
      dye -= sye - source.height
      sye = source.height
    }

    // Check if there is any area visible
    if (dxs < width && dxe > 0 &&
      dys < height && dye > 0 &&
      dxs < dxe && dys < dye) {

      val sourceData = source.data
      val destData = data
      val sourceWidth = source.width

      if (!alpha) {
        // No alpha, blit fast
        val lineLen = dxe - dxs
        var sy = sys
        var dy = dys
        while (dy < dye) {
          val si = sy * sourceWidth + sxs
          val di = dy * width + dxs

          System.arraycopy(sourceData, si, destData, di, lineLen)

          dy += 1
          sy += 1
        }

      } else {
        // Alpha overlay
        var dy = dys
        var dx = dxs
        var sy = sys
        var sx = sxs
        var si = 0
        var di = 0
        while (dy < dye) {
          dx = dxs
          sx = sxs
          di = dy * width + dx
          si = sy * sourceWidth + sx
          while (dx < dxe) {

            // Get source and destination colors
            val sc = sourceData(si)
            val dc = destData(di)

            // Get source and dest alphas
            val sAlpha = (sc >> 24) & 0xff
            val dAlpha = (dc >> 24) & 0xff
            var inverseSAlpha = 255 - sAlpha

            // Calculate source over destination color mix
            val a = sAlpha + dAlpha * inverseSAlpha / 255

            if (a == 0) {
              // All colors and alpha zero
              destData(di) = 0
            } else {
              val r = (((sc >> 16) & 0xff) * sAlpha + (((dc >> 16) & 0xff) * dAlpha * inverseSAlpha) / 255) / a
              val g = (((sc >> 8) & 0xff) * sAlpha + (((dc >> 8) & 0xff) * dAlpha * inverseSAlpha) / 255) / a
              val b = (((sc >> 0) & 0xff) * sAlpha + (((dc >> 0) & 0xff) * dAlpha * inverseSAlpha) / 255) / a

              // Set new color to destination
              destData(di) =
                ((a & 0xFF) << 24) |
                  ((r & 0xFF) << 16) |
                  ((g & 0xFF) << 8) |
                  ((b & 0xFF) << 0)
            }


            // Update loop indexes
            di += 1
            si += 1
            dx += 1
            sx += 1
          }
          dy += 1
          sy += 1
        }
      }

      updatedArea.includeArea(dxs, dys, dxe, dye)
    }
  }

  /**
   * Calculates a new value for each pixel in the raster using the specified filter code.
   * The filter is passed the raster itself, picture coordinates and color of the point, and returns the new color for the point.
   */
  @inline final def filter(filterFunction: (Raster, Int, Int, Int) => Int) {
    filter(0, 0, width, height)(filterFunction)
  }

  /**
   * Calculates a new value for each pixel in the specified area using the specified filter code.
   * The filter is passed the raster itself, picture coordinates and color of the point, and returns the new color for the point.
   */
  @inline final def filter(area: Area)(filterFunction: (Raster, Int, Int, Int) => Int) {
    filter(area.x1, area.y1, area.w, area.h)(filterFunction)
  }

  /**
   * Calculates a new value for each pixel in the specified area using the specified filter code.
   * The filter is passed the raster itself, picture coordinates and color of the point, and returns the new color for the point.
   */
  @inline final def filter(x: Int, y: Int, w: Int, h: Int)(filterFunction: (Raster, Int, Int, Int) => Int) {
    val xs = math.max(x, 0)
    val ys = math.max(y, 0)
    val xe = math.min(x + w, width)
    val ye = math.min(y + h, height)

    if (xs < xe && ys < ye) {
      var tx = xs
      var ty = ys
      var i = 0
      while (ty < ye) {
        tx = xs
        i = ty * width + tx
        while (tx < xe) {
          data(i) = filterFunction(this, tx, ty, data(i))

          tx += 1
          i += 1
        }
        ty += 1
      }

      updatedArea.includeArea(xs, ys, xe, ye)
    }
  }

  /**
   * Draws a colored rectangle.
   */
  @inline final def drawRect(area: Area, color: Int) {
    drawRect(area.x1, area.y1, area.w, area.h, color, false)
  }

  /**
   * Draws a colored rectangle.
   */
  @inline final def drawRect(area: Area, color: Int, alpha: Boolean) {
    drawRect(area.x1, area.y1, area.w, area.h, color, alpha)
  }

  /**
   * Draws a colored rectangle.
   */
  @inline final def drawRect(x: Int, y: Int, w: Int, h: Int, color: Int, alpha: Boolean = false) {
    val xs = math.max(x, 0)
    val ys = math.max(y, 0)
    val xe = math.min(x + w, width)
    val ye = math.min(y + h, height)

    if (xs < xe && ys < ye) {
      var tx = xs
      var ty = ys
      var i = 0

      if (alpha) {
        // Fill with alpha-blended color

        val sAlpha = (color >> 24) & 0xff
        val scr = ((color >> 16) & 0xff) * sAlpha
        val scg = ((color >> 8) & 0xff) * sAlpha
        val scb = ((color >> 0) & 0xff) * sAlpha

        while (ty < ye) {
          tx = xs
          i = ty * width + tx
          while (tx < xe) {

            // Get destination color
            val dc = data(i)

            // Get dest alpha
            val dAlpha = (dc >> 24) & 0xff
            var inverseSAlpha = 255 - sAlpha

            // Calculate source over destination color mix
            val a = sAlpha + dAlpha * inverseSAlpha / 255
            if (a == 0) {
              // All colors and alpha zero
              data(i) = 0
            }
            else {
              val r = (scr + (((dc >> 16) & 0xff) * dAlpha * inverseSAlpha) / 255) / a
              val g = (scg + (((dc >> 8) & 0xff) * dAlpha * inverseSAlpha) / 255) / a
              val b = (scb + (((dc >> 0) & 0xff) * dAlpha * inverseSAlpha) / 255) / a

              // Set new color to destination
              data(i) =
                ((a & 0xFF) << 24) |
                  ((r & 0xFF) << 16) |
                  ((g & 0xFF) << 8) |
                  ((b & 0xFF) << 0)
            }

            tx += 1
            i += 1
          }
          ty += 1
        }
      }
      else {
        // Fill with solid value
        val lineLen = xe - xs
        while (ty < ye) {
          i = ty * width + xs

          Arrays.fill(data, i, i + lineLen, color)

          ty += 1
        }
      }


      updatedArea.includeArea(xs, ys, xe, ye)
    }
  }

  def getArea: Area = new Area(area)

  def getUpdatedArea: Area = updatedArea

  def clearUpdatedArea() {
    updatedArea.clear()
  }

  def markDirty() {
    updatedArea.setArea(area)
  }

}



