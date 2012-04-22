package net.zzorn.gameflow

import entity.Entity

/**
 *
 */
trait CollisionHandler[A <: Entity, B <: Entity] {

  def onCollision(entityA: A, entityB: B)

}