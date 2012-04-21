package net.zzorn.gameflow.input

/**
 *
 */
trait InputEvent {
}

case class KeyDown(keyCode: Int) extends InputEvent
case class KeyUp(keyCode: Int) extends InputEvent