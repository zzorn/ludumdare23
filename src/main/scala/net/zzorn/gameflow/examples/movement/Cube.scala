package net.zzorn.gameflow.examples.movement

import net.zzorn.gameflow.entity.Entity2D
import net.zzorn.utils.{ColorUtils, Vec2}
import java.awt.{Graphics2D, Color}

/**
 * Simple cube entity.
 */
class Cube(gravity: Vec2, fadeintime: Double, airresistance: Double) extends Entity2D() {

  val size = Vec2()
  val baseSize = Vec2()
  var color: Color = Color.RED
  var scale = 0.0

  init()

  def init() {
    scale = 0
    pos.x = math.random * 1000 - 300
    pos.y = math.random * 1000 - 300
    baseSize.x = math.random * 80
    baseSize.y = math.random * 80
    color = new Color(ColorUtils.HSLtoRGB(
      math.random.toFloat * 0.25f + 0.9f,
      math.random.toFloat * 0.5f + 0.1f,
      math.random.toFloat * 0.3f + 0.15f,
      math.random.toFloat * 0.3f + 0.4f), false)
    velocity.x = (math.random - 0.5) * 400
    velocity.y = (math.random - 0.5) * 400
    thrust.set(gravity)
    calculateSize()
  }

  override def update(durationSeconds: Double) {
    velocity +*=(gravity, durationSeconds)
    velocity *= math.max(0, 1.0 - airresistance * durationSeconds)
    pos +*=(velocity, durationSeconds)
    if (scale < 1.0) scale = scale * (1.0 - durationSeconds / fadeintime) + 1.0 * durationSeconds / fadeintime
    if (pos.y > 1000) init()
    calculateSize()
  }

  override def draw(g: Graphics2D, screenW: Int, screenH: Int, x: Int, y: Int) {
    g.setColor(color)
    g.fillRect(x, y, size.x.toInt, size.y.toInt)
  }


  private def calculateSize() {
    size.set(baseSize)
    size.x *= math.abs(velocity.x) * 0.03 * scale
    size.y *= math.abs(velocity.y) * 0.04 * scale
  }

}
