package net.zzorn.gameflow.camera

import net.zzorn.utils.Vec3
import net.zzorn.gameflow.camera.Camera

/**
 * A camera that just focuses on a specified point.
 */
class StationaryCamera(val initialCameraPos: Vec3 = Vec3()) extends Camera {


  cameraPos.set(initialCameraPos)


}