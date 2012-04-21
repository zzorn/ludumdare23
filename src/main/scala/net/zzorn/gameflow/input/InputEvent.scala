package net.zzorn.gameflow.input

/**
 *
 */
trait InputEvent {
}

case class KeyPressed(keyCode: Int) extends InputEvent
case class KeyReleased(keyCode: Int) extends InputEvent
case class MouseMoved(mouseX: Int, mouseY: Int) extends InputEvent
case class MousePressed(button: MouseButton, mouseX: Int, mouseY: Int) extends InputEvent
case class MouseReleased(button: MouseButton, mouseX: Int, mouseY: Int) extends InputEvent


sealed trait MouseButton
case object NoMouseButton extends MouseButton
case object LeftMouseButton extends MouseButton
case object RightMouseButton extends MouseButton
case object MiddleMouseButton extends MouseButton
case object UnknownMouseButton extends MouseButton

