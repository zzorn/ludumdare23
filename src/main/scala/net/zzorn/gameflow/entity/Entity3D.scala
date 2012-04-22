package net.zzorn.gameflow.entity

import net.zzorn.utils.{Vec3}
import java.awt.Graphics2D
import net.zzorn.gameflow.gamemap.GameMap


/**
 *
 */
class Entity3D extends Entity {

  var gameMap: GameMap = null
  val pos      = Vec3()
  val velocity = Vec3()
  val thrust   = Vec3()

  def setGameMap(gameMap: GameMap) {
    this.gameMap = gameMap
  }

  def getGameMap: GameMap = gameMap

  def update(durationSeconds: Double) {
    velocity +*= (thrust, durationSeconds)
    pos      +*= (velocity, durationSeconds)
  }

  def draw(g: Graphics2D, screenW: Int, screenH: Int, x: Int, y: Int, scale: Double) {
  }
}