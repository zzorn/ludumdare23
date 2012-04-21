package net.zzorn.gameflow

import java.awt.Graphics2D

/**
 * Something that can be rendered to the screen.
 */
trait Rendering {

  def render(g: Graphics2D, screenW: Int, screenH: Int)

}