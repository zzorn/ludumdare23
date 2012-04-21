package net.zzorn.gameflow.gamemap

import net.zzorn.utils.{Vec3, Vec2}


/**
 * A camera that specifies what point of a level the view should focus on.
 */
trait Camera {

  def update(seconds: Double)

  def cameraPos: Vec3

}