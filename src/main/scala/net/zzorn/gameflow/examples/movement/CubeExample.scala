package net.zzorn.gameflow.examples.movement

import scala.math
import java.awt.{Graphics2D, Color}

import net.zzorn.gameflow.GameBase
import net.zzorn.gameflow.entity.{Entity3D, Entity2D}
import net.zzorn.utils.{Vec2, Vec3, ColorUtils}
import net.zzorn.gameflow.gamemap.camera.WobbleCamera
import net.zzorn.gameflow.gamemap.GameMap

/**
 * Example of drawing and moving entity classes.
 */
object CubeExample extends GameBase("Cube Example") {

  val gravity = Vec2(0, 200)
  val fadeintime = 5.0
  val airresistance = 0.5

  private val gameMap = new GameMap(new WobbleCamera(wobbleSize = Vec3(40, 10), wobbleSpeed = 4))

  override protected def init() {

    // Add cubes
    var i = 0
    while (i < 1000) {
      gameMap.add(new Cube(gravity, fadeintime, airresistance))
      i += 1
    }
  }

  override protected def update(durationSec: Double) {
    gameMap.update(durationSec)
  }


  override protected def draw(screen: Graphics2D, screenW: Int, screenH: Int) {
    gameMap.draw(screen, screenW, screenH)
  }

}