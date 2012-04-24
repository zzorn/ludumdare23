package net.zzorn.gameflow

import camera.Camera
import java.awt.Graphics2D
import scala.collection.JavaConversions._
import java.util.{LinkedHashSet, HashSet, ArrayList}

/**
 * Handles facets
 */
class FacetManager extends Facet {

  private var facets: ArrayList[Facet] = new ArrayList()
  private var facetsToAdd: LinkedHashSet[Facet] = new LinkedHashSet()
  private var facetsToRemove: LinkedHashSet[Facet] = new LinkedHashSet()

  final def addFacet(facet: Facet) {
    if (facets.contains(facet)) throw new IllegalArgumentException("Can not add facet '"+facet+"' twice")

    facetsToAdd.add(facet)
    facetsToRemove.remove(facet)
  }

  final def removeFacet(facet: Facet) {
    if (!facets.contains(facet)) throw new IllegalArgumentException("Can not remove facet '"+facet+"', not found")

    facetsToAdd.remove(facet)
    facetsToRemove.add(facet)
  }

  def update(durationSeconds: Double) {
    // De-init removed facets
    facetsToRemove foreach {facet =>
      facet.deInit()
      facets.remove(facet)
    }
    facetsToRemove.clear()

    // Init added facets
    facetsToAdd foreach {facet =>
      facets.add(facet)
      facet.init()
    }
    facetsToAdd.clear()

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