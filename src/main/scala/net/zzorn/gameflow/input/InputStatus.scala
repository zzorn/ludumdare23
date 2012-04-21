package net.zzorn.gameflow.input

/**
 *
 */
trait InputStatus {

  def isKeyHeld(keyCode: Int): Boolean
  def isMouseButtonHeld(mouseButton: MouseButton): Boolean

  def addListener(listener: InputListener)
  def removeListener(listener: InputListener)

  def NumKeys: Int
  def NumButtons: Int

  def getMouseButtonForNumber(button: Int): MouseButton
  def getMouseButtonNumber(button: MouseButton ): Int

}