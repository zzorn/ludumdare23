package net.zzorn.gameflow.gamemap.camera

import net.zzorn.gameflow.entity.Entity
import net.zzorn.utils.{ParameterChecker, Vec2, Vec3}
import net.zzorn.gameflow.gamemap.Camera

/**
 * A camera that tracks a specified entity, focusing a bit ahead of it.
 */
class TrackingCamera(initialTrackedEntity: Entity,
                     var leadingAmount: Double = 10,
                     var trackingSpeed: Double = 10) extends Camera {

  val cameraPos = Vec3()

  private var _trackedEntity: Entity = null
  private val targetPos = Vec3()
  private val targetVelocity = Vec3()

  setTrackedEntity(initialTrackedEntity)

  def trackedEntity: Entity = _trackedEntity

  def setTrackedEntity(trackedEntity: Entity) {
    ParameterChecker.requireNotNull(trackedEntity, 'trackedEntity)

    _trackedEntity = trackedEntity
    trackedEntity.getPos(cameraPos)
  }

  def update(seconds: Double) {
    _trackedEntity.getPos(targetPos)
    _trackedEntity.getVelocity(targetVelocity)
    targetPos +*=(targetVelocity, leadingAmount)
    cameraPos.mixWith(targetPos, math.min(1.0, seconds * trackingSpeed))
  }

}