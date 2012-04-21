package net.zzorn.gameflow

/**
 * Something that can be called from the main loop.
 */
trait Facet extends Updating with Rendering {

  def init()

  def deInit()

}