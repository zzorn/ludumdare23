package net.zzorn.utils

import net.zzorn.utils.Raster
import javax.swing.JPanel
import java.awt.Graphics
import java.awt.event.{ComponentAdapter, ComponentEvent}

/**
 *
 */
class RawImagePanel extends JPanel {

  private var _rawImage: RawImage = null

  addComponentListener(new ComponentAdapter {

    override def componentShown(e: ComponentEvent) {
      if (_rawImage == null) buildRawImage()
    }

    override def componentResized(e: ComponentEvent) {
      buildRawImage()
    }
  })

  def rawImage: RawImage = _rawImage
  def raster: Raster = _rawImage.raster

  override def paintComponent(g: Graphics) {
    _rawImage.renderToGraphics(g, _rawImage.raster.getUpdatedArea)
    _rawImage.raster.clearUpdatedArea()
  }


  def buildRawImage() {
    _rawImage = new RawImage(getWidth, getHeight)
    _rawImage.raster.markDirty()
  }


}
