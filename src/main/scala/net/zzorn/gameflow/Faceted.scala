package net.zzorn.gameflow

import camera.Camera
import java.awt.Graphics2D
import java.util.ArrayList
import scala.collection.JavaConversions._

/**
 * Handles facets
 */
class FacetManager extends Facet {

  private var facets: ArrayList[Facet] = new ArrayList()
  private var facetsToInit: ArrayList[Facet] = new ArrayList()
  private var facetsToDeInit: ArrayList[Facet] = new ArrayList()

  final def addFacet(facet: Facet) {
    if (facets.contains(facet)) throw new IllegalArgumentException("Can not add facet '"+facet+"' twice")

    facets.add(facet)
    facetsToInit.add(facet)
  }

  final def removeFacet(facet: Facet) {
    if (!facets.contains(facet)) throw new IllegalArgumentException("Can not remove facet '"+facet+"', not found")

    facets.remove(facet)
    facetsToDeInit.add(facet)
  }

  def update(durationSeconds: Double) {
    // Init added facets
    facetsToDeInit foreach {facet =>
      facet.deInit()
    }
    facetsToDeInit.clear()

    // De-init removed facets
    facetsToInit foreach {facet =>
      facet.init()
    }
    facetsToInit.clear()

    // Update contained facets
    facets foreach {facet =>
      facet.update(durationSeconds)
    }
  }

  def init() {
  }

  def deInit() {
    facets foreach {facet =>
      facet.deInit()
    }
  }


  def render(g: Graphics2D, camera: Camera) {
    facets foreach {facet =>
      facet.render(g, camera)
    }
  }

}