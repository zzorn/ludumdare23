package net.zzorn.gameflow.examples.movement

import java.awt.{Graphics2D, Color}
import net.zzorn.utils.{Vec3, ColorUtils, Vec2}
import net.zzorn.gameflow.entity.{Entity3D, Entity}

/**
 * Simple cube entity.
 */
class Cube(gravity: Vec3, fadeintime: Double, airresistance: Double) extends Entity3D() {


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

  override def draw(g: Graphics2D, screenW: Int, screenH: Int, x: Int, y: Int, scale: Double) {
    g.setColor(color)
    var w = (size.x * scale).toInt
    var h = (size.y * scale).toInt
    g.fillRect(x - w/2, y - h/2, w, h)
  }


  private def calculateSize() {
    size.set(baseSize)
    size.x *= math.abs(velocity.x) * 0.03 * scale
    size.y *= math.abs(velocity.y) * 0.04 * scale
  }

}
