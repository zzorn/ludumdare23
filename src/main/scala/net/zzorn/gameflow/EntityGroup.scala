package net.zzorn.gameflow

import camera.Camera
import entity.Entity
import scala.collection.JavaConversions._
import java.awt.Graphics2D
import net.zzorn.utils.{Vec3, Vec2}
import java.util.{HashMap, Collections, HashSet, ArrayList}

/**
 *
 */
class EntityGroup[T <: Entity]() extends Facet {

  private val screenPos = Vec2()
  private val worldPos = Vec3()

  private val _entities: ArrayList[T] = new ArrayList[T]()
  private val _entitiesToAdd: HashSet[T] = new HashSet[T]()
  private val _entitiesToRemove: HashSet[Entity] = new HashSet[Entity]()
  private val unmodifiableList: java.util.List[T] = Collections.unmodifiableList(_entities)

  private val collisionHandlers: HashSet[(EntityGroup[_ <: Entity], CollisionHandler[_ <: T, _ <: Entity])] = new HashSet[(EntityGroup[_ <: Entity], CollisionHandler[_ <: T, _ <: Entity])]()
  def entities: java.util.List[T] = unmodifiableList

  final def add(entity: T) {
    _entitiesToAdd.add(entity)
    _entitiesToRemove.remove(entity)
  }

  final def remove(entity: Entity) {
    _entitiesToRemove.add(entity)
    _entitiesToAdd.remove(entity)
  }

  final def clear() {
    _entitiesToRemove.addAll(_entities)
  }

  def getColliding(other: T): T = {
    _entities foreach {entity =>
      if (other.overlaps(entity)) return entity
    }
    null.asInstanceOf[T]
  }

  def getFirstOverlappingEntity(location: Vec3, radius: Double): T = {
    _entities foreach {entity =>
      if (entity.overlaps(location, radius)) return entity
    }
    null.asInstanceOf[T]
  }

  def handleCollisions(otherGroup: EntityGroup[_ <: Entity], handler: CollisionHandler[_ <: T, _ <: Entity]) {
    _entities foreach {entity =>
      var otherEntity: Entity = otherGroup.getFirstOverlappingEntity(entity.pos, entity.getRadius)
      if (otherEntity != null) {
        handler.asInstanceOf[CollisionHandler[Entity, Entity]].onCollision(entity, otherEntity)
      }
    }
  }

  def onCollideWith(otherGroup: EntityGroup[_ <: Entity], handler: CollisionHandler[_ <: T, _ <: Entity]) {
    collisionHandlers.add((otherGroup, handler))
  }

  def removeCollisionHandler(otherGroup: EntityGroup[_ <: Entity], handler: CollisionHandler[_ <: T, _ <: Entity]) {
    collisionHandlers.remove((otherGroup, handler))
  }

  def init() {}

  def deInit() {}

  override def update(seconds: Double) {
    // Add any entities to add
    _entitiesToAdd foreach {entity =>
      _entities.add(entity)
      entity.setGroup(this)
      onEntityAdded(entity)
    }
    _entitiesToAdd.clear()

    // Remove any entities to remove
    _entitiesToRemove foreach {entity =>
      onEntityRemoved(entity.asInstanceOf[T])
      entity.setGroup(null)
      _entities.remove(entity)
    }
    _entitiesToRemove.clear()

    // Update entities, map, and camera
    _entities foreach {entity =>
      entity.update(seconds)
    }

    // Handle collisions
    collisionHandlers foreach {groupAndHandler =>
      handleCollisions(groupAndHandler._1, groupAndHandler._2)
    }
  }


  def render(g: Graphics2D, camera: Camera) {
    _entities.foreach {entity =>
      screenPos.zero()
      worldPos.set(entity.pos)
      camera.worldPosToScreenPos(worldPos, screenPos)

      drawEntity(g, camera.screenW, camera.screenH, entity, screenPos.x.toInt, screenPos.y.toInt, camera.cameraScale)
    }
  }

  protected def drawEntity(g: Graphics2D, screenW: Int, screenH: Int, entity: T, x: Int, y: Int, scale: Double) {
    entity.draw(g, screenW, screenH, x, y, scale)
  }

  protected def onEntityAdded(entity: T) {}

  protected def onEntityRemoved(entity: T) {}

}