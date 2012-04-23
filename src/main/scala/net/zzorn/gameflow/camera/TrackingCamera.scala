package net.zzorn.gameflow.camera

import net.zzorn.gameflow.entity.Entity
import net.zzorn.utils.{ParameterChecker, Vec2, Vec3}

/**
 * A camera that tracks a specified entity, focusing a bit ahead of it.
 */
class TrackingCamera(initialTrackedEntity: Entity,
                     var leadingAmount: Double = 10,
                     var trackingSpeed: Double = 10) extends Camera {

  private var _trackedEntity: Entity = null
  private val targetPos = Vec3()
  private val entityPos = Vec3()
  private val oldEntityPos = Vec3()

  setTrackedEntity(initialTrackedEntity)

  def trackedEntity: Entity = _trackedEntity

  def setTrackedEntity(trackedEntity: Entity) {
    _trackedEntity = trackedEntity
    if (_trackedEntity != null) {
      oldEntityPos.set(_trackedEntity.pos)
      entityPos.set(_trackedEntity.pos)
      targetPos.set(_trackedEntity.pos)
      cameraPos.set(_trackedEntity.pos)
    }
    else{
      cameraPos.set(0,0,0)
    }
  }

  override def update(seconds: Double) {
    if (_trackedEntity != null) {
      oldEntityPos.set(entityPos)
      entityPos.set(_trackedEntity.pos)

      // Calculate target
      targetPos.set(entityPos)
      targetPos +*=(_trackedEntity.velocity, leadingAmount)

      // Move towards target
      cameraPos.mixWith(targetPos, math.min(1.0, seconds * trackingSpeed))
    }
  }

}
