package net.zzorn.utils

import java.util.Random

object Vec2 {

  private val r = new Random()

  def random(): Vec2 = {
    Vec2(
      r.nextDouble() * 2 - 1,
      r.nextDouble() * 2 - 1
    )
  }

  def random(seed: Long): Vec2 = {
    r.setSeed(seed)
    Vec2(
      r.nextDouble() * 2 - 1,
      r.nextDouble() * 2 - 1
    )
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
  def distance(other: Vec2): Double = {
    val dx = x - other.x
    val dy = y - other.y
    math.sqrt(dx*dx + dy*dy)
  }

  @inline
  def normalize: Vec2 = {
    val len = length
    if (len == 0) Vec2(0,0)
    else this / len
  }

  @inline
  def setNormalized() {
    val len = math.sqrt(x*x + y*y)
    if (len == 0) {
      x = 1
      y = 0
    }
    else {
      x /= len
      y /= len
    }
  }

  @inline
  def dot(other: Vec2): Double = {
    x*other.x + y*other.y
  }

  @inline
  def angleBetween(other: Vec2): Double = {
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
  def setPlus (other: Vec2) {
    += (other)
  }

  @inline
  def setMinus (other: Vec2) {
    -= (other)
  }

  @inline
  def setMul (other: Vec2) {
    *= (other)
  }

  @inline
  def setDiv (other: Vec2) {
    /= (other)
  }

  @inline
  def setPlusMul (other: Vec2, scalar: Double) {
    +*= (other, scalar)
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
