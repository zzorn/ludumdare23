package net.zzorn.gameflow.entity

import java.awt.Graphics2D
import scala.Double
import net.zzorn.utils.{Vec3, Vec2}
import net.zzorn.gameflow.{EntityGroup, Updating}

/**
 * Something that can be drawn and updated.
 */
trait Entity extends Updating {

  def setGroup(group: EntityGroup[_ <: Entity])
  def getGroup: EntityGroup[_ <: Entity]

  /**
   * Removes the entity itself from the group it is in
   */
  def remove() {
    var group = getGroup
    if (group != null) group.remove(this)
  }

  def pos: Vec3
  def velocity: Vec3

  def draw(g: Graphics2D, screenW: Int, screenH: Int, x: Int, y: Int, scale: Double)

  def getRadius: Double = 1

  def surfaceDistance(other: Entity): Double = {
    pos.distance(other.pos) - getRadius - other.getRadius
  }

  def overlaps(other: Entity): Boolean = {
    surfaceDistance(other) < 0
  }

  def overlaps(location: Vec3, radius: Double): Boolean = {
    pos.distance(location) - getRadius - radius < 0
  }

}
