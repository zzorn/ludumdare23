package net.zzorn.gameflow.entity

import net.zzorn.utils.{Vec3}
import java.awt.Graphics2D


/**
 *
 */
class Entity3D extends Entity{

  val pos      = Vec3()
  val velocity = Vec3()
  val thrust   = Vec3()

  def update(durationSeconds: Double) {
    velocity +*= (thrust, durationSeconds)
    pos      +*= (velocity, durationSeconds)
  }

  def draw(g: Graphics2D, screenW: Int, screenH: Int, x: Int, y: Int, scale: Double) {
  }
}