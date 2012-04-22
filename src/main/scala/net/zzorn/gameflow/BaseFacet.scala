package net.zzorn.gameflow

import camera.Camera
import java.awt.Graphics2D

/**
 *
 */
class BaseFacet extends Facet {

  def init() {}

  def update(durationSeconds: Double) {}

  def render(g: Graphics2D, camera: Camera) {}

  def deInit() {}

}