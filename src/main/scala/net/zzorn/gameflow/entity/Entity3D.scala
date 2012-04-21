package net.zzorn.gameflow.entity

import net.zzorn.utils.{Vec3}


/**
 *
 */
trait Entity3D extends Entity{

  val pos      = Vec3()
  val velocity = Vec3()
  val thrust   = Vec3()

  def update(durationSeconds: Double) {
    velocity +*= (thrust, durationSeconds)
    pos      +*= (velocity, durationSeconds)
  }

  def getPos(worldPosOut: Vec3) {
    worldPosOut.set(pos)
  }

  def getVelocity(velocityOut: Vec3) {
    velocityOut.set(velocity)
  }

}