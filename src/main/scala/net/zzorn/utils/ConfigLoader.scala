package net.zzorn.utils


import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.constructor.Constructor._
import org.yaml.snakeyaml.constructor.SafeConstructor
import org.yaml.snakeyaml.introspector.BeanAccess
import org.yaml.snakeyaml.composer.Composer
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.Representer
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.constructor.{Constructor, SafeConstructor}
import org.yaml.snakeyaml.constructor.Constructor._
import org.yaml.snakeyaml.representer.Representer
import org.yaml.snakeyaml.TypeDescription
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.{TypeDescription, Yaml}
import scala.Predef._
import java.lang.Class._
import java.io._

/**
 * Loads some kind of configuration from a yaml file.
 */
class ConfigLoader[C <: AnyRef](configBaseClass: Class[C], allowedTypes: Class[_ <: AnyRef] * ) {

  private val constructor = new FilterConstructor(configBaseClass)
  private val representer = new Representer()

  for (allowedType <- allowedTypes ) {
    allowType(Manifest.classType(allowedType))
  }

  def allowType[T <: AnyRef](implicit m: Manifest[T]) {
    val kind: Class[T] = m.erasure.asInstanceOf[Class[T]]
    constructor.registerType[T]
    representer.addClassTag(kind, new Tag("!" + kind.getSimpleName))
  }

  def loadConfig(file: File): C = {
    loadConfig(new InputStreamReader(new FileInputStream(file)), file.getName)
  }

  def loadConfig(configName: String): C = {
    loadConfig(new InputStreamReader(getClass.getClassLoader.getResource(configName).openStream()), configName)
  }

  def loadConfig(inputStreamReader: InputStreamReader, sourceName: String): C = {
    println("Loading models from " + sourceName)

    constructor.source = sourceName
    val yaml = new Yaml(constructor, representer)
    try {
      yaml.load(inputStreamReader).asInstanceOf[C]
    }
    catch {
      case e: Exception =>
        throw new Error("Error while loading '"+sourceName+"': " + e.getMessage, e)
    }
    finally {
      if (inputStreamReader != null) inputStreamReader.close()
    }
  }


  def saveConfig(config: C, output: Writer) {
    val yaml = new Yaml(constructor, representer)
    yaml.dump(config, output)
  }


  class FilterConstructor(rootClass: Class[_ <: AnyRef]) extends Constructor(rootClass) with Logging {

    private var mappings: Map[String, Class[_ <: AnyRef ]] = Map()

    var source = "unknown"

    def registerType[T <: AnyRef](implicit m: Manifest[T]) {

      val kind: Class[T] = m.erasure.asInstanceOf[Class[T]]
      val tag: String = "!" + m.erasure.getSimpleName
      addTypeDescription(new TypeDescription(kind, tag))
      mappings += kind.getName -> kind

      log.info("Registered tag '"+tag+"' for type '"+kind.getName +"'")
      //println("Registered tag '"+tag+"' for type '"+kind.getName +"'")
    }

    override def getClassForName(name: String): Class[_] = {

      if (mappings.contains(name)) mappings(name)
      else throw FilterException(name, source)
    }

    /**
     * Exception used if non-permitted class is loaded.
     */
    case class FilterException(forbiddenClass: String, source: String)
      extends Error("The class '"+forbiddenClass+"' is not permitted, but it was used in "+source)


  }

}
