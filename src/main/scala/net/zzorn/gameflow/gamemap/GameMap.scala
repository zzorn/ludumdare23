package net.zzorn.gameflow.gamemap


import net.zzorn.gameflow.entity.Entity
import scala.collection.JavaConversions._
import java.util.{List, Collections, ArrayList}
import net.zzorn.utils.{Vec2, Vec3}
import java.awt.{Color, Graphics2D}
import net.zzorn.gameflow.camera.{StationaryCamera, Camera}
import net.zzorn.gameflow.{EntityGroup, BaseFacet, Facet}

/**
 * Basic game map class.
 */
class GameMap() extends EntityGroup {

  override final def update(seconds: Double) {
    super.update(seconds)

    // Update map
    updateMap(seconds)
  }


  override def render(g: Graphics2D, camera: Camera) {
    drawMapBackground(g, camera)
    super.render(g, camera)
  }

  protected def updateMap(seconds: Double) {}

  protected def drawMapBackground(g: Graphics2D, camera: Camera) {
    g.setColor(Color.BLACK)
    g.fillRect(0, 0, camera.screenW, camera.screenH)
  }

}

