package net.zzorn.gameflow.gamemap

import camera.StationaryCamera
import net.zzorn.gameflow.entity.Entity
import scala.collection.JavaConversions._
import java.util.{List, Collections, ArrayList}
import net.zzorn.utils.{Vec2, Vec3}
import java.awt.{Color, Graphics2D}

/**
 * Basic game map class.
 */
class GameMap(var camera: Camera = new StationaryCamera()) {

  private val _entities: ArrayList[Entity] = new ArrayList[Entity]()
  private val unmodifiableList: List[Entity] = Collections.unmodifiableList(_entities)
  private val screenPos = Vec2()
  private val worldPos = Vec3()

  def entities: java.util.List[Entity] = unmodifiableList

  final def update(seconds: Double) {
    _entities foreach {e => e.update(seconds)}
    updateMap(seconds)
    camera.update(seconds)
  }

  final def draw(g: Graphics2D, screenW: Int, screenH: Int) {
    drawMap(g, screenW, screenH, unmodifiableList)
  }

  final def add(entity: Entity) {
    _entities.add(entity)
    onEntityAdded(entity)
  }

  final def remove(entity: Entity) {
    onEntityRemoved(entity)
    _entities.remove(entity)
  }

  def worldPosToScreenPos(worldPos: Vec3, screenPosOut: Vec2) {
    screenPos.x = worldPos.x
    screenPos.y = worldPos.y
  }

  protected def updateMap(seconds: Double) {}

  protected def onEntityAdded(entity: Entity) {}

  protected def onEntityRemoved(entity: Entity) {}

  protected def drawMap(g: Graphics2D, screenW: Int, screenH: Int, entities: java.util.List[Entity]) {
    drawMapBackground(g, screenW, screenH)

    val cameraPos = camera.cameraPos

    entities.foreach {e =>
      worldPos.zero()
      screenPos.zero()
      e.getPos(worldPos)
      worldPos -= cameraPos
      worldPosToScreenPos(worldPos, screenPos)
      drawEntity(g, screenW, screenH, e, screenPos.x.toInt, screenPos.y.toInt)
    }
  }

  protected def drawEntity(g: Graphics2D, screenW: Int, screenH: Int, entity: Entity, x: Int, y: Int) {
    entity.draw(g, screenW, screenH, x, y)
  }

  protected def drawMapBackground(g: Graphics2D, screenW: Int, screenH: Int) {
    g.setColor(Color.BLACK)
    g.fillRect(0, 0, screenW, screenH)
  }

}

