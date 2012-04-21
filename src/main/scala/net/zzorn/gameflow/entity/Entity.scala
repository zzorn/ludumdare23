package net.zzorn.gameflow.entity

import java.awt.Graphics2D
import scala.Double
import net.zzorn.utils.{Vec3, Vec2}
import net.zzorn.gameflow.Updating

/**
 * Something that can be drawn and updated.
 */
trait Entity extends Updating {

  def pos: Vec3
  def velocity: Vec3

  def draw(g: Graphics2D, screenW: Int, screenH: Int, x: Int, y: Int)

}
