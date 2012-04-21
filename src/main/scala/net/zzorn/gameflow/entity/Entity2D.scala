package net.zzorn.gameflow.entity

import net.zzorn.utils.{Vec3, Vec2}


/**
 *
 */
trait Entity2D extends Entity {

  val pos      = Vec2()
  val velocity = Vec2()
  val thrust   = Vec2()

  def update(durationSeconds: Double) {
    velocity +*= (thrust, durationSeconds)
    pos      +*= (velocity, durationSeconds)
  }


  def getPos(worldPosOut: Vec3) {
    worldPosOut.set(pos.x, pos.y, 0)
  }

  def getVelocity(velocityOut: Vec3) {
    velocityOut.set(velocity.x, velocity.y, 0)
  }
}