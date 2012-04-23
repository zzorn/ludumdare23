package net.zzorn.gameflow.input

/**
 *
 */
trait InputListener {

  /**
   * Called when a key is pressed down.
   */
  def onKeyPressed(key: Int, inputStatus: InputStatus, durationSeconds: Double) {}

  /**
   * Called when a key is released.
   */
  def onKeyReleased(key: Int, inputStatus: InputStatus, durationSeconds: Double) {}

  /**
   * Called when the mouse is moved
   */
  def onMouseMoved(x: Int, y: Int, inputStatus: InputStatus, durationSeconds: Double) {}

  /**
   * Called when the mouse wheel is moved
   */
  def onMouseWheelMoved(wheelMovement: Int, x: Int, y: Int, inputStatus: InputStatus, durationSeconds: Double) {}

  /**
   * Called when a mouse button is pressed.
   */
  def onMouseButtonPressed(button: MouseButton, x: Int, y: Int, inputStatus: InputStatus, durationSeconds: Double) {}

  /**
   * Called when a mouse button is released.
   */
  def onMouseButtonReleased(button: MouseButton, x: Int, y: Int, inputStatus: InputStatus, durationSeconds: Double) {}

  /**
   * Called when some keyboard key or mouse button has been pressed or released.
   * @param inputStatus an InputStatus object that can be queried for currently held keys and buttons
   */
  def onKeysUpdated(inputStatus: InputStatus, durationSeconds: Double) {}

}