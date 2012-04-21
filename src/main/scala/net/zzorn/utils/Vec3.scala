package net.zzorn.utils

import scala.math
import java.util.Random

object Vec3 {

  private val r = new Random()

  def random(): Vec3 = {
    Vec3(r.nextDouble(), r.nextDouble(), r.nextDouble())
  }

  def random(seed: Long): Vec3 = {
    r.setSeed(seed)
    Vec3(r.nextDouble(), r.nextDouble(), r.nextDouble())
  }

}

/**
 * Mutable 3D coordinate.
 */
final case class Vec3(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0) {

  @inline
  def zero() {
    x = 0
    y = 0
    z = 0
  }

  @inline
  def set(x: Double, y: Double, z: Double) {
    this.x = x
    this.y = y
    this.z = z
  }

  @inline
  def set (other: Vec3) {
    this.x = other.x
    this.y = other.y
    this.z = other.z
  }

  @inline
  def mixWith(other: Vec3, amount: Double = 0.5) {
    val selfAmount = 1.0 - amount
    x = x * selfAmount + other.x * amount
    y = y * selfAmount + other.y * amount
    z = z * selfAmount + other.z * amount
  }

  @inline
  def length: Double = {
    math.sqrt(x*x + y*y + z*z)
  }

  @inline
  def lengthSquared: Double = {
    x*x + y*y + z*z
  }

  @inline
  def distance(other: Vec3): Double = {
    val dx = x - other.x
    val dy = y - other.y
    val dz = z - other.z
    math.sqrt(dx*dx + dy*dy + dz*dz)
  }

  @inline
  def normalize: Vec3 = {
    val len = length
    if (len == 0) Vec3(0,0,0)
    else this / len
  }

  @inline
  def normalizeLocal() {
    val len = math.sqrt(x*x + y*y + z*z)
    if (len == 0) {
      x = 1
      y = 0
      z = 0
    }
    else {
      x /= len
      y /= len
      z /= len
    }
  }

  @inline
  def dot(other: Vec3): Double = {
    x*other.x +
    y*other.y +
    z*other.z
  }

  @inline
  def angleBetween(other: Vec3): Double = {
    val dotProd = dot(other)
    val divisor: Double = length * other.length
    if (divisor == 0) 0
    else math.acos(dotProd / divisor)
  }

  @inline
  def setX(x: Double) {
    this.x = x
  }

  @inline
  def setY(y: Double) {
    this.y = y
  }

  @inline
  def setZ(z: Double) {
    this.z = z
  }

  @inline
  def setPlus (scalar: Double) {
    += (scalar)
  }

  @inline
  def setMinus (scalar: Double) {
    -= (scalar)
  }

  @inline
  def setMul (scalar: Double) {
    *= (scalar)
  }

  @inline
  def setDiv (scalar: Double) {
    /= (scalar)
  }

  @inline
  def setPlus (other: Vec3) {
    += (other)
  }

  @inline
  def setMinus (other: Vec3) {
    -= (other)
  }

  @inline
  def setMul (other: Vec3) {
    *= (other)
  }

  @inline
  def setDiv (other: Vec3) {
    /= (other)
  }

  @inline
  def setPlusMul (other: Vec3, scalar: Double) {
    +*= (other, scalar)
  }


  @inline
  def + (scalar: Double): Vec3 = {
    Vec3(x + scalar,
         y + scalar,
         z + scalar)
  }

  @inline
  def - (scalar: Double): Vec3 = {
    Vec3(x - scalar,
         y - scalar,
         z - scalar)
  }

  @inline
  def * (scalar: Double): Vec3 = {
    Vec3(x * scalar,
         y * scalar,
         z * scalar)
  }

  @inline
  def / (scalar: Double): Vec3 = {
    Vec3(x / scalar,
         y / scalar,
         z / scalar)
  }

  @inline
  def + (other: Vec3): Vec3 = {
    Vec3(x + other.x,
         y + other.y,
         z + other.z)
  }

  @inline
  def - (other: Vec3): Vec3 = {
    Vec3(x - other.x,
         y - other.y,
         z - other.z)
  }

  @inline
  def * (other: Vec3): Vec3 = {
    Vec3(x * other.x,
         y * other.y,
         z * other.z)
  }

  @inline
  def / (other: Vec3): Vec3 = {
    Vec3(x / other.x,
         y / other.y,
         z / other.z)
  }


  @inline
  def += (scalar: Double) {
    x += scalar
    y += scalar
    z += scalar
  }

  @inline
  def -= (scalar: Double) {
    x -= scalar
    y -= scalar
    z -= scalar
  }

  @inline
  def *= (scalar: Double) {
    x *= scalar
    y *= scalar
    z *= scalar
  }

  @inline
  def /= (scalar: Double) {
    x /= scalar
    y /= scalar
    z /= scalar
  }

  @inline
  def += (other: Vec3) {
    x += other.x
    y += other.y
    z += other.z
  }

  @inline
  def -= (other: Vec3) {
    x -= other.x
    y -= other.y
    z -= other.z
  }

  @inline
  def *= (other: Vec3) {
    x *= other.x
    y *= other.y
    z *= other.z
  }

  @inline
  def /= (other: Vec3) {
    x /= other.x
    y /= other.y
    z /= other.z
  }

  @inline
  def +*= (other: Vec3, otherScale: Double) {
    x += other.x * otherScale
    y += other.y * otherScale
    z += other.z * otherScale
  }


  override def toString = "("+x+", "+y+", "+z+")"
}