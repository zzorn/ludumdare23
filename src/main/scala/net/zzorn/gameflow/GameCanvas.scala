package net.zzorn.gameflow

import java.awt.image.BufferStrategy
import java.awt.{Graphics2D, Canvas}

/**
 * Handles rendering game content.
 * According to http://www.cokeandcode.com/info/tut2d.html , Canvas is the only component supporting hardware acceleration.
 */
class GameCanvas extends Canvas {


  private var _bufferStrategy: BufferStrategy = null

  def setup() {
    // Disable automatic repainting, we'll do that ourselves in accelerated mode.
    setIgnoreRepaint(true)

    // Setup accelerated, double buffered graphics
    createBufferStrategy(2)
    _bufferStrategy = getBufferStrategy
  }

  def acceleratedSurface: Graphics2D = {
    _bufferStrategy.getDrawGraphics.asInstanceOf[Graphics2D]
  }

  def flipPage() {
    _bufferStrategy.show()
  }
}