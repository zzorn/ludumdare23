package net.zzorn.gameflow.input

import scala.collection.JavaConversions._
import java.awt.Graphics2D
import net.zzorn.gameflow.{BaseFacet, Facet, Updating}
import java.awt.event._
import java.util.{HashSet, ArrayList, HashMap}


/**
 *
 */
class InputHandler extends BaseFacet with KeyListener with MouseListener with MouseMotionListener with InputStatus {

  val NumKeys = 256
  val NumButtons = 4

  private val pressedKeys  = new Array[Boolean](NumKeys)
  private val releasedKeys = new Array[Boolean](NumKeys)
  private val heldKeys     = new Array[Boolean](NumKeys)
  private val pressedButtons  = new Array[Boolean](NumButtons)
  private val releasedButtons = new Array[Boolean](NumButtons)
  private val heldButtons     = new Array[Boolean](NumButtons)
  private var mouseX       = 0
  private var mouseY       = 0
  private var mouseMoved   = false

  private var listeners: ArrayList[InputListener] = new ArrayList[InputListener]()

  def isKeyHeld(keyCode: Int): Boolean = {
    if (keyCode >= 0 && keyCode < NumKeys) heldKeys(keyCode)
    else false
  }

  def isMouseButtonHeld(mouseButton: MouseButton): Boolean = {
    val buttonNum = getMouseButtonNumber(mouseButton)
    if (buttonNum >= 0 && buttonNum < NumButtons) heldButtons(buttonNum)
    else false
  }


  def addListener(listener: InputListener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener)
    }
  }

  def removeListener(listener: InputListener) {
    listeners.remove(listener)
  }

  override def keyPressed(e: KeyEvent) {
    var key = e.getKeyCode
    if (key >= 0 && key < NumKeys) {
      heldKeys(key)    = true
      pressedKeys(key) = true
    }
  }

  override def keyReleased(e: KeyEvent) {
    var key = e.getKeyCode
    if (key >= 0 && key < NumKeys) {
      heldKeys(key)     = false
      releasedKeys(key) = true
    }
  }

  override def mousePressed(e: MouseEvent) {
    var button: Int = e.getButton
    if (button >= 0 && button < NumButtons) {
      heldButtons(button)    = true
      pressedButtons(button) = true
      mouseX = e.getX
      mouseY = e.getY
    }
    mouseMoved = true
  }

  override def mouseReleased(e: MouseEvent) {
    var button: Int = e.getButton
    if (button >= 0 && button < NumButtons) {
      heldButtons(button)     = false
      releasedButtons(button) = true
      mouseX = e.getX
      mouseY = e.getY
    }
    mouseMoved = true
  }

  override def mouseMoved(e: MouseEvent) {
    mouseX = e.getX
    mouseY = e.getY
    mouseMoved = true
  }

  override def mouseDragged(e: MouseEvent) {
    mouseX = e.getX
    mouseY = e.getY
    mouseMoved = true
  }

  // Ignore these
  def mouseClicked(e: MouseEvent) {}
  def mouseEntered(e: MouseEvent) {}
  def mouseExited(e: MouseEvent) {}
  def keyTyped(e: KeyEvent) {}


  override def update(durationSeconds: Double) {

    // Notify about key presses
    var keyOrButtonChanged = false
    var key = 0
    while (key < NumKeys) {
      if (heldKeys(key)) {
        // Key likely pressed after key released, send any press events last
        keyOrButtonChanged ||= checkAndNotifyKeyRelease(key, durationSeconds)
        keyOrButtonChanged ||= checkAndNotifyKeyPress(key, durationSeconds)
      }
      else {
        // Key likely released after key pressed, send any release events last
        keyOrButtonChanged ||= checkAndNotifyKeyPress(key, durationSeconds)
        keyOrButtonChanged ||= checkAndNotifyKeyRelease(key, durationSeconds)
      }
      key += 1
    }

    // Notify about mouse movement
    val x = mouseX
    val y = mouseY
    if (mouseMoved) {
      mouseMoved = false
      listeners foreach {_.onMouseMoved(x, y, this, durationSeconds)}
    }

    // Notify about mouse presses
    var button = 0
    while (button < NumButtons) {
      val mouseButton = getMouseButtonForNumber(button)
      if (heldButtons(button)) {
        // Button likely pressed after released, send any press events last
        keyOrButtonChanged ||= checkAndNotifyButtonRelease(button, mouseButton, x, y, durationSeconds)
        keyOrButtonChanged ||= checkAndNotifyButtonPress(button, mouseButton, x, y, durationSeconds)
      }
      else {
        // Button likely released after pressed, send any release events last
        keyOrButtonChanged ||= checkAndNotifyButtonPress(button, mouseButton, x, y, durationSeconds)
        keyOrButtonChanged ||= checkAndNotifyButtonRelease(button, mouseButton, x, y, durationSeconds)
      }
      button += 1
    }

    // Notify about key or button changes
    if (keyOrButtonChanged) {
      listeners foreach { _.onKeysUpdated(this, durationSeconds) }
    }


  }


  private def checkAndNotifyButtonPress(key: Int, mouseButton: MouseButton, x: Int, y: Int, durationSeconds: Double): Boolean = {
    if (pressedButtons(key)) {
      listeners foreach { _.onMouseButtonPressed(mouseButton, x, y, this, durationSeconds) }
      pressedButtons(key) = false
      true
    }
    else false
  }

  private def checkAndNotifyButtonRelease(key: Int, mouseButton: MouseButton, x: Int, y: Int, durationSeconds: Double): Boolean = {
    if (releasedButtons(key)) {
      listeners foreach { _.onMouseButtonReleased(mouseButton, x, y, this, durationSeconds) }
      releasedButtons(key) = false
      true
    }
    else false
  }

  private def checkAndNotifyKeyPress(key: Int, durationSeconds: Double): Boolean = {
    if (pressedKeys(key)) {
      listeners foreach { _.onKeyPressed(key, this, durationSeconds) }
      pressedKeys(key) = false
      true
    }
    else false
  }

  private def checkAndNotifyKeyRelease(key: Int, durationSeconds: Double): Boolean = {
    if (releasedKeys(key)) {
      listeners foreach { _.onKeyReleased(key, this, durationSeconds) }
      releasedKeys(key) = false
      true
    }
    else false
  }



  final def getMouseButtonForNumber(button: Int): MouseButton = {
    button match {
      case MouseEvent.NOBUTTON => NoMouseButton
      case MouseEvent.BUTTON1  => LeftMouseButton
      case MouseEvent.BUTTON2  => MiddleMouseButton
      case MouseEvent.BUTTON3  => RightMouseButton
      case _                   => UnknownMouseButton
    }
  }

  final def getMouseButtonNumber(button: MouseButton ): Int = {
    button match {
      case LeftMouseButton => MouseEvent.BUTTON1
      case MiddleMouseButton => MouseEvent.BUTTON2
      case RightMouseButton => MouseEvent.BUTTON3
      case _ => MouseEvent.NOBUTTON
    }
  }



}


