package net.zzorn.gameflow.entity

import net.zzorn.utils.{Vec3}
import java.awt.Graphics2D
import net.zzorn.gameflow.EntityGroup


/**
 *
 */
class Entity3D extends Entity {

  var group: EntityGroup[_ <: Entity] = null
  val pos      = Vec3()
  val velocity = Vec3()
  val thrust   = Vec3()


  def setGroup(gameMap: EntityGroup[_ <: Entity]) {
    this.group = gameMap
  }

  def getGroup: EntityGroup[_ <: Entity] = group

  /**
   * Removes the entity itself from the group it is in
   */
  override def remove() {
    var group = getGroup
    if (group != null) group.remove(this)
  }


  def update(durationSeconds: Double) {
    velocity +*= (thrust, durationSeconds)
    pos      +*= (velocity, durationSeconds)
  }

  def draw(g: Graphics2D, screenW: Int, screenH: Int, x: Int, y: Int, scale: Double) {
  }
}