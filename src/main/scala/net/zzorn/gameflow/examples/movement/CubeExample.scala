package net.zzorn.gameflow.examples.movement

import scala.math
import java.awt.{Graphics2D, Color}

import net.zzorn.utils.{Vec2, Vec3, ColorUtils}
import net.zzorn.gameflow.gamemap.GameMap
import net.zzorn.gameflow.camera.{Camera, WobbleCamera}
import net.zzorn.gameflow.{EntityGroup, GameBase}

/**
 * Example of drawing and moving entity classes.
 */
object CubeExample extends GameBase("Cube Example") {

  val gravity = Vec3(0, 200,0)
  val fadeintime = 5.0
  val airresistance = 0.5

  private val cubeGroup = new EntityGroup[Cube]()

  def main(args: Array[String]) {
    CubeExample.start()
  }

  override protected def init() {

    setCamera(new WobbleCamera(wobbleSize = Vec3(40, 10), wobbleSpeed = 4))

    // Add cubes
    var i = 0
    while (i < 1000) {
      cubeGroup.add(new Cube(gravity, fadeintime, airresistance))
      i += 1
    }

    // Update and draw all cubes
    addFacet(cubeGroup)

  }


}