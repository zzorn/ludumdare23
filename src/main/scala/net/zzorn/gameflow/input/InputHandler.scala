package net.zzorn.gameflow.input

import java.util.{ArrayList, HashSet, HashMap}
import scala.collection.JavaConversions._
import java.awt.Graphics2D
import net.zzorn.gameflow.{BaseFacet, Facet, Updating}
import java.awt.event._
import net.zzorn.gameflow.input.InputListener


/**
 *
 */
class InputHandler extends BaseFacet with KeyListener with MouseListener with MouseMotionListener {

  private val pressedKeys = new HashSet[Int]()
  private val pressedMouseButtons = new HashSet[MouseButton]()
  private var listeners: List[InputListener] = Nil
  private val queuedEvents: ArrayList[InputEvent] = new ArrayList[InputEvent](100)
  private val recentEvents: ArrayList[InputEvent] = new ArrayList[InputEvent](100)
  private var lastDown: Long = 0
  private val inputLock = new Object()

  def addListener(listener: InputListener) {
    inputLock synchronized {
      listeners ::= listener
    }
  }


  override def keyPressed(e: KeyEvent) {
    inputLock synchronized {
      if (!pressedKeys.contains(e.getKeyCode) && lastDown != e.getWhen) {
        queuedEvents.add(KeyPressed(e.getKeyCode))
        pressedKeys.add(e.getKeyCode)
        lastDown = e.getWhen
      }
    }
  }

  override def keyReleased(e: KeyEvent) {
    inputLock synchronized {
      if (pressedKeys.contains(e.getKeyCode) && lastDown != e.getWhen) {
        queuedEvents.add(KeyReleased(e.getKeyCode))
        pressedKeys.remove(e.getKeyCode)
      }
    }
  }

  def mousePressed(e: MouseEvent) {
    inputLock synchronized {
      val button: MouseButton = getButton(e)
      queuedEvents.add(MousePressed(button, e.getX, e.getY))
      pressedMouseButtons.add(button)
    }
  }

  def mouseReleased(e: MouseEvent) {
    inputLock synchronized {
      val button: MouseButton = getButton(e)
      queuedEvents.add(MouseReleased(button, e.getX, e.getY))
      pressedMouseButtons.remove(button)
    }
  }

  def mouseDragged(e: MouseEvent) {
    inputLock synchronized {
      queuedEvents.add(MouseMoved(e.getX, e.getY))
    }
  }

  def mouseMoved(e: MouseEvent) {
    inputLock synchronized {
      queuedEvents.add(MouseMoved(e.getX, e.getY))
    }
  }


  // Ignore these
  def mouseClicked(e: MouseEvent) {}
  def mouseEntered(e: MouseEvent) {}
  def mouseExited(e: MouseEvent) {}
  def keyTyped(e: KeyEvent) {}


  override def update(durationSeconds: Double) {

    // Retrieve and clear queue
    inputLock synchronized {
      recentEvents.addAll(queuedEvents)
      queuedEvents.clear()
    }

    // Notify listeners
    var keysUpdated = false
    recentEvents foreach {event =>
      println(event)
      event match {
        case event: MouseMoved =>
          listeners foreach {listener =>
            listener.onMouseMoved(event, this, durationSeconds)
          }

        case event: KeyPressed =>
          keysUpdated = true
          listeners foreach {listener =>
            listener.onKeyPressed(event, this, durationSeconds)
          }

        case event: KeyReleased =>
          keysUpdated = true
          listeners foreach {listener =>
            listener.onKeyReleased(event, this, durationSeconds)
          }

        case event: MousePressed =>
          keysUpdated = true
          listeners foreach {listener =>
            listener.onMouseButtonPressed(event, this, durationSeconds)
          }

        case event: MouseReleased =>
          keysUpdated = true
          listeners foreach {listener =>
            listener.onMouseButtonReleased(event, this, durationSeconds)
          }

      }
    }

    if (keysUpdated) {
      listeners foreach {listener =>
        listener.onKeysUpdated(this, durationSeconds)
      }
    }

    recentEvents.clear()
  }



  def isPressed(keyCode: Int): Boolean = {
    var contains: Boolean = false
    inputLock synchronized {
      contains = pressedKeys.contains(keyCode)
    }
    contains
  }

  def isMouseButtonPressed(mouseButton: MouseButton): Boolean = {
    var contains: Boolean = false
    inputLock synchronized {
      contains = pressedMouseButtons.contains(mouseButton)
    }
    contains
  }


  private final def getButton(event: MouseEvent): MouseButton = {
    event.getButton match {
      case MouseEvent.NOBUTTON => NoMouseButton
      case MouseEvent.BUTTON1  => LeftMouseButton
      case MouseEvent.BUTTON2  => MiddleMouseButton
      case MouseEvent.BUTTON3  => RightMouseButton
      case _                   => UnknownMouseButton
    }
  }
}


