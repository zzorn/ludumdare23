package net.zzorn.gameflow.input

/**
 *
 */
trait InputListener {

  /**
   * Called when a key is pressed down.
   * @param keyCode one of the VK_ codes in KeyEvent
   */
  def onKeyPressed(keyCode: Int, keyHandler: KeyHandler, durationSeconds: Double) {}

  /**
   * Called when a key is released.
   * @param keyCode one of the VK_ codes in KeyEvent
   */
  def onKeyReleased(keyCode: Int, keyHandler: KeyHandler, durationSeconds: Double) {}

  /**
   * Called when some key has been pressed or released.
   * @param keyHandler the handler that keeps track of pressed keys.
   */
  def onKeysUpdated(keyHandler: KeyHandler, durationSeconds: Double) {}

}