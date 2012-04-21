package net.zzorn.gameflow.camera

import net.zzorn.utils.{Vec3, Vec2}



/**
 * A camera that specifies what point of a level the view should focus on.
 */
trait Camera {

  private val tempPos = Vec3()

  var cameraScale: Double = 1.0
  val cameraPos: Vec3 = Vec3()

  def update(seconds: Double) {}

  def worldPosToScreenPos(worldPos: Vec3, screenPosOut: Vec2, screenW: Int, screenH: Int) {
    tempPos.set(worldPos)
    tempPos -= cameraPos
    tempPos /= cameraScale

    tempPos.x += screenW / 2
    tempPos.y += screenH / 2

    screenPosOut.x = tempPos.x
    screenPosOut.y = tempPos.y
  }

}