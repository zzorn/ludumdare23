package net.zzorn.gameflow.gamemap.camera

import net.zzorn.gameflow.gamemap.Camera
import net.zzorn.gameflow.examples.movement.CubeExample._
import net.zzorn.utils.{SimplexNoise, Vec3}

/**
 * A camera implementation that just wobbles around a point, or another camera.
 * Can be used e.g. for camera shake effects.
 * The wobble size, speed, and other parameters can be changed on the fly.
 */
class WobbleCamera(val focusOffset: Vec3 = Vec3(),
                   val wobbleSize:  Vec3 = Vec3(100, 100),
                   var wobbleSpeed: Double = 10.0,
                   var originalCamera: Camera = null) extends Camera {

  private var elapsedSeconds = 0.0
  private val wobbleOffset = Vec3.random() * 100

  var cameraPos = Vec3()

  def update(seconds: Double) {
    // Start from the original camera if available, otherwise origo
    if (originalCamera != null) {
      originalCamera.update(seconds)
      cameraPos.set(originalCamera.cameraPos)
    }
    else {
      cameraPos.zero()
    }

    // Add offset (mostly useful when we do not have an original camera)
    cameraPos += focusOffset

    // Add the wobble!
    elapsedSeconds += seconds
    cameraPos.x += SimplexNoise.noise(elapsedSeconds / wobbleSpeed, wobbleOffset.x) * wobbleSize.x
    cameraPos.y += SimplexNoise.noise(elapsedSeconds / wobbleSpeed, wobbleOffset.y) * wobbleSize.y
    cameraPos.z += SimplexNoise.noise(elapsedSeconds / wobbleSpeed, wobbleOffset.z) * wobbleSize.z
  }
}