package net.zzorn.gameflow.camera

import net.zzorn.utils.{Vec3, Vec2}



/**
 * A camera that specifies what point of a level the view should focus on.
 */
trait Camera {

  private val tempPos = Vec3()

  var screenW: Int = 0
  var screenH: Int = 0

  var cameraScale: Double = 1.0
  val cameraPos: Vec3 = Vec3()

  def setCameraScale(scale: Double) {
    cameraScale = scale
  }

  def setScreenSize(w: Int, h: Int) {
    screenW = w
    screenH = h
  }

  def update(seconds: Double) {}

  def screenPosToWorldPos(screenX: Double, screenY: Double, worldPosOut: Vec3) {
    tempPos.set(screenX, screenY, 0)
    tempPos.x -= screenW / 2
    tempPos.y -= screenH / 2
    tempPos /= cameraScale
    tempPos += cameraPos

    worldPosOut.x = tempPos.x
    worldPosOut.y = tempPos.y
    worldPosOut.z = 0
  }

  def worldPosToScreenPos(worldPos: Vec3, screenPosOut: Vec2) {
    tempPos.set(worldPos)
    tempPos -= cameraPos
    tempPos *= cameraScale

    tempPos.x += screenW / 2
    tempPos.y += screenH / 2

    screenPosOut.x = tempPos.x
    screenPosOut.y = tempPos.y
  }

}