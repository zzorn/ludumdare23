package net.zzorn.gameflow.gamemap.camera

import net.zzorn.utils.Vec3
import net.zzorn.gameflow.gamemap.Camera

/**
 * A camera that just focuses on a specified point.
 */
class StationaryCamera(val cameraPos: Vec3 = Vec3()) extends Camera {

  def update(seconds: Double) {
  }

}