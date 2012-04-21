package net.zzorn.gameflow.input

/**
 * Useful for debugging, prints events and status
 */
class PrintingInputListener extends InputListener {

  override def onKeyPressed(key: Int, inputStatus: InputStatus, durationSeconds: Double) {
    outputKey(key, "pressed")
  }

  override def onKeyReleased(key: Int, inputStatus: InputStatus, durationSeconds: Double) {
    outputKey(key, "released")
  }

  override def onMouseMoved(x: Int, y: Int, inputStatus: InputStatus, durationSeconds: Double) {
    outputMouse(x, y)
  }

  override def onMouseButtonPressed(button: MouseButton, x: Int, y: Int, inputStatus: InputStatus, durationSeconds: Double) {
    outputMouseButton(button, "pressed")
  }

  override def onMouseButtonReleased(button: MouseButton, x: Int, y: Int, inputStatus: InputStatus, durationSeconds: Double) {
    outputMouseButton(button, "released")
  }

  override def onKeysUpdated(inputStatus: InputStatus, durationSeconds: Double) {
    print("Keys Pressed: ")
    var key = 0
    while (key < inputStatus.NumKeys) {
      if (inputStatus.isKeyHeld(key)) print(key + "  ")
      key+=1
    }

    print("  Mosue Buttons Pressed: ")
    var button = 0
    while (button  < inputStatus.NumButtons) {
      var mouseButton = inputStatus.getMouseButtonForNumber(button)
      if (inputStatus.isMouseButtonHeld(mouseButton)) print(mouseButton + "  ")
      button +=1
    }

    println()
  }


  def outputKey(keyCode: Int, status: String) {
    println("Key "+ keyCode + ": " + status)
  }

  def outputMouseButton(button: MouseButton, status: String) {
    println("MouseButton "+ button + ": " + status)
  }

  def outputMouse(x: Int, y: Int) {
    println("Mouse "+ x + ", " + y)
  }
}