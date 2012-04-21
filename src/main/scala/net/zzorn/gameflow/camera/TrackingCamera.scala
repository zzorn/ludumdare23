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
  private val targetVelocity = Vec3()

  setTrackedEntity(initialTrackedEntity)

  def trackedEntity: Entity = _trackedEntity

  def setTrackedEntity(trackedEntity: Entity) {
    ParameterChecker.requireNotNull(trackedEntity, 'trackedEntity)

    _trackedEntity = trackedEntity
    cameraPos.set(trackedEntity.pos)
  }

  override def update(seconds: Double) {
    targetPos.set(_trackedEntity.pos)
    targetVelocity.set(_trackedEntity.velocity)

    targetPos +*=(targetVelocity, leadingAmount)
    cameraPos.mixWith(targetPos, math.min(1.0, seconds * trackingSpeed))
  }

}