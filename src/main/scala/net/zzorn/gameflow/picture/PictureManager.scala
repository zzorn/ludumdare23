package net.zzorn.gameflow.picture

import java.util.HashMap
import javax.imageio.ImageIO
import java.awt.{Transparency, GraphicsEnvironment, Image}
import java.awt.image.BufferedImage
import net.zzorn.utils.ImageUtils

/**
 * Loads pictures and caches them.
 */
class PictureManager(picturePath: String = "") {

  private val pictures: HashMap[String, Picture] = new HashMap[String, Picture]()

  def get(name: String): Picture = {
    var picture: Picture = pictures.get(name)
    if (picture == null) {
      picture = loadPicture(name)
    }
    picture
  }

  def loadPicture(name: String): Picture = {
    var resourceLocation: String = picturePath + name
    val url = this.getClass.getClassLoader.getResource(resourceLocation);
    if (url == null) throw new IllegalStateException("Could not find image with resource location '"+resourceLocation+"'")

    val pic = new BufferedImagePicture(ImageUtils.createScreenCompatibleImage(ImageIO.read(url)))
    pictures.put(name, pic)
    pic
  }


}