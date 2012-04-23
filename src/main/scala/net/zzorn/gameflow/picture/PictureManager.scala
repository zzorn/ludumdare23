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

  private val images: HashMap[String, BufferedImage] = new HashMap[String, BufferedImage]()
  private val pictures: HashMap[String, Picture] = new HashMap[String, Picture]()

  def getImage(name: String): BufferedImage = {
    if (!images.containsKey(name)) loadPicture(name)
    images.get(name)
  }

  def get(name: String): Picture = {
    if (!pictures.containsKey(name)) loadPicture(name)
    pictures.get(name)
  }

  def get(name: String, scale: Double): Picture = {
    if (scale == 1.0) get(name)
    else ScaledPicture(get(name), scale)
  }

  private def loadPicture(name: String): Picture = {
    val resourceLocation: String = picturePath + name
    val url = this.getClass.getClassLoader.getResource(resourceLocation);
    if (url == null) throw new IllegalStateException("Could not find image with resource location '"+resourceLocation+"'")

    val bufferedImage: BufferedImage = ImageUtils.createScreenCompatibleImage(ImageIO.read(url))
    val pic = new BufferedImagePicture(bufferedImage)
    pictures.put(name, pic)
    images.put(name, bufferedImage)
    pic
  }


}