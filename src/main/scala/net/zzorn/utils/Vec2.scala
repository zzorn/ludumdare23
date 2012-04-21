package net.zzorn.utils

import java.util.Random
import net.zzorn.utils.Vec3._

object Vec2 {

  private val r = new Random()

  def random(): Vec2 = {
    Vec2(r.nextDouble(), r.nextDouble())
  }

  def random(seed: Long): Vec2 = {
    r.setSeed(seed)
    Vec2(r.nextDouble(), r.nextDouble())
  }

}


/**
 * Mutable 2D coordinate.
 */
final case class Vec2(var x: Double = 0.0, var y: Double = 0.0) {

  @inline
  def zero() {
    x = 0
    y = 0
  }

  @inline
  def set(x: Double, y: Double) {
    this.x = x
    this.y = y
  }

  @inline
  def set (other: Vec2) {
    this.x = other.x
    this.y = other.y
  }

  @inline
  def mixWith(other: Vec2, amount: Double = 0.5) {
    val selfAmount = 1.0 - amount
    x = x * selfAmount + other.x * amount
    y = y * selfAmount + other.y * amount
  }

  @inline
  def length: Double = {
    math.sqrt(x*x + y*y)
  }

  @inline
  def lengthSquared: Double = {
    x*x + y*y
  }

  @inline
  def normalize: Vec2 = {
    val len = length
    if (len == 0) Vec2(0,0)
    else this / len
  }

  @inline
  def normalizeLocal() {
    val len = math.sqrt(x*x + y*y)
    if (len == 0) {
      x = 0
      y = 0
    }
    else {
      x /= len
      y /= len
    }
  }


  @inline
  def + (scalar: Double): Vec2 = {
    Vec2(x + scalar,
      y + scalar)
  }

  @inline
  def - (scalar: Double): Vec2 = {
    Vec2(x - scalar,
      y - scalar)
  }

  @inline
  def * (scalar: Double): Vec2 = {
    Vec2(x * scalar,
      y * scalar)
  }

  @inline
  def / (scalar: Double): Vec2 = {
    Vec2(x / scalar,
      y / scalar)
  }

  @inline
  def + (other: Vec2): Vec2 = {
    Vec2(x + other.x,
      y + other.y)
  }

  @inline
  def - (other: Vec2): Vec2 = {
    Vec2(x - other.x,
      y - other.y)
  }

  @inline
  def * (other: Vec2): Vec2 = {
    Vec2(x * other.x,
      y * other.y)
  }

  @inline
  def / (other: Vec2): Vec2 = {
    Vec2(x / other.x,
      y / other.y)
  }


  @inline
  def += (scalar: Double) {
    x += scalar
    y += scalar
  }

  @inline
  def -= (scalar: Double) {
    x -= scalar
    y -= scalar
  }

  @inline
  def *= (scalar: Double) {
    x *= scalar
    y *= scalar
  }

  @inline
  def /= (scalar: Double) {
    x /= scalar
    y /= scalar
  }

  @inline
  def += (other: Vec2) {
    x += other.x
    y += other.y
  }

  @inline
  def -= (other: Vec2) {
    x -= other.x
    y -= other.y
  }

  @inline
  def *= (other: Vec2) {
    x *= other.x
    y *= other.y
  }

  @inline
  def /= (other: Vec2) {
    x /= other.x
    y /= other.y
  }

  @inline
  def +*= (other: Vec2, otherScale: Double) {
    x += other.x * otherScale
    y += other.y * otherScale
  }


  override def toString = "("+x+", "+y+")"

}
