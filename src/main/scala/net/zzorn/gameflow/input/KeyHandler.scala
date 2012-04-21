package net.zzorn.gameflow.input

import net.zzorn.gameflow.input.InputListener
import java.util.{ArrayList, HashSet, HashMap}
import scala.collection.JavaConversions._
import java.awt.Graphics2D
import net.zzorn.gameflow.{BaseFacet, Facet, Updating}
import java.awt.event.{KeyListener, KeyEvent, KeyAdapter}


/**
 *
 */
class KeyHandler extends BaseFacet with KeyListener {

  private val pressedKeys = new HashSet[Int]()
  private var listeners: List[InputListener] = Nil
  private val queuedEvents: ArrayList[InputEvent] = new ArrayList[InputEvent](100)
  private var lastDown: Long = 0
  private val keyReadLock = new Object()

  def addListener(listener: InputListener) {
    keyReadLock synchronized {
      listeners ::= listener
    }
  }


  def keyTyped(e: KeyEvent) {}

  override def keyPressed(e: KeyEvent) {
    keyReadLock synchronized {
      if (!pressedKeys.contains(e.getKeyCode) && lastDown != e.getWhen) {
        queuedEvents.add(KeyDown(e.getKeyCode))
        pressedKeys.add(e.getKeyCode)
        lastDown = e.getWhen
      }
    }
  }

  override def keyReleased(e: KeyEvent) {
    keyReadLock synchronized {
      if (pressedKeys.contains(e.getKeyCode) && lastDown != e.getWhen) {
        queuedEvents.add(KeyUp(e.getKeyCode))
        pressedKeys.remove(e.getKeyCode)
      }
    }
  }


  override def update(durationSeconds: Double) {
    keyReadLock synchronized {
      queuedEvents foreach {event =>
        event match {
          case down: KeyDown =>
            listeners foreach {listener =>
              listener.onKeyPressed(down.keyCode, this, durationSeconds)
            }

          case up: KeyUp =>
            listeners foreach {listener =>
              listener.onKeyReleased(up.keyCode, this, durationSeconds)
            }
        }
      }

      if (!queuedEvents.isEmpty) {
        listeners foreach {listener =>
          listener.onKeysUpdated(this, durationSeconds)
        }
      }

      queuedEvents.clear()
    }
  }



  def isPressed(keyCode: Int): Boolean = {
    var contains: Boolean = false
    keyReadLock synchronized {
      contains = pressedKeys.contains(keyCode)
    }
    contains
  }

}


