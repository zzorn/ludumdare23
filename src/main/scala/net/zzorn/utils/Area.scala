package net.zzorn.utils

import math.min
import math.max

/**
 * Axis aligned rectangular area.
 *
 * Includes minX and minY, excludes maxX, maxY.
 */
final class Area() {

  private var empty = true
  private var minX : Int = 0
  private var minY : Int = 0
  private var maxX : Int = 0
  private var maxY : Int = 0

  def this(other: Area) {
    this()
    setArea(other)
  }

  def this(x1: Int, y1: Int, x2: Int, y2: Int) {
    this()
    setArea(x1, y1, x2, y2)
  }

  def x1 = minX
  def y1 = minY
  def x2 = maxX
  def y2 = maxY
  def w = getWidth
  def h = getHeight

  def isEmpty = empty
  def getMinX = minX
  def getMinY = minY
  def getMaxX = maxX
  def getMaxY = maxY

  def getWidth = if (empty) 0 else maxX - minX
  def getHeight = if (empty) 0 else maxY - minY

  def clear() {
    empty = true
    minX = 0
    minY = 0
    maxX = 0
    maxY = 0
  }

  def includePoint( x : Int, y : Int ) {
    if (empty) {
      minX = x
      minY = y
      maxX = x + 1
      maxY = y + 1
      empty = false
    }
    else {
      minX = min(minX, x)
      minY = min(minY, y)
      maxX = max(maxX, x + 1)
      maxY = max(maxY, y + 1)
    }
  }

  /**
   * Extends this BoundingBox to include the specified other area.
   */
  def includeArea(other: Area) {
    includeArea(other.getMinX, other.getMinY, other.getMaxX, other.getMaxY)
  }

  /**
   * Extends this BoundingBox to include the specified area.
   * The order of x1,y1 and x2,y2 does not matter.
   */
  def includeArea( x1 : Int, y1 : Int, x2 : Int, y2 : Int ) {
    if (empty) {
      minX = min( x1, x2 )
      minY = min( y1, y2 )
      maxX = max( x1, x2 )
      maxY = max( y1, y2 )
      empty = false
    }
    else {
      minX = min( min( minX, x1 ), x2 )
      minY = min( min( minY, y1 ), y2 )
      maxX = max( max( maxX, x1 ), x2 )
      maxY = max( max( maxY, y1 ), y2 )
    }
  }

  def setArea(other: Area) {
    setArea(other.x1, other.y1,
            other.x2, other.y2)
  }

  def setArea(x1: Int, y1: Int, x2: Int, y2: Int) {
    clear()
    includeArea(x1, y1, x2, y2)
  }

  def contains( x : Int, y : Int ): Boolean = {
    if (empty) false
    else minX <= x && x < maxX &&
         minY <= y && y < maxY
  }

}

