package net.zzorn.gameflow.input

/**
 *
 */
class InputListenerAdapter extends InputListener {

  override def onKeyPressed(key: Int, inputStatus: InputStatus, durationSeconds: Double) {}
  override def onKeyReleased(key: Int, inputStatus: InputStatus, durationSeconds: Double) {}
  override def onMouseMoved(x: Int, y: Int, inputStatus: InputStatus, durationSeconds: Double) {}
  override def onMouseButtonPressed(button: MouseButton, x: Int, y: Int, inputStatus: InputStatus, durationSeconds: Double) {}
  override def onMouseButtonReleased(button: MouseButton, x: Int, y: Int, inputStatus: InputStatus, durationSeconds: Double) {}
  override def onKeysUpdated(inputStatus: InputStatus, durationSeconds: Double) {}

}