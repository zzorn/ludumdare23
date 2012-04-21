package net.zzorn.gameflow.input

/**
 *
 */
trait InputListener {

  /**
   * Called when a key is pressed down.
   */
  def onKeyPressed(event: KeyPressed, keyHandler: InputHandler, durationSeconds: Double) {}

  /**
   * Called when a key is released.
   */
  def onKeyReleased(event: KeyReleased, keyHandler: InputHandler, durationSeconds: Double) {}

  /**
   * Called when the mouse is moved
   */
  def onMouseMoved(event: MouseMoved, keyHandler: InputHandler, durationSeconds: Double) {}

  /**
   * Called when a mouse button is pressed.
   */
  def onMouseButtonPressed(event: MousePressed, keyHandler: InputHandler, durationSeconds: Double) {}

  /**
   * Called when a mouse button is released.
   */
  def onMouseButtonReleased(event: MouseReleased, keyHandler: InputHandler, durationSeconds: Double) {}

  /**
   * Called when some key has been pressed or released.
   * @param keyHandler the handler that keeps track of pressed keys.
   */
  def onKeysUpdated(keyHandler: InputHandler, durationSeconds: Double) {}

}