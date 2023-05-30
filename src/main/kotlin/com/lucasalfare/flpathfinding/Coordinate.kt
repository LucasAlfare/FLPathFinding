package com.lucasalfare.flpathfinding

/**
 * Represents a coordinate inside a bidimensional space.
 */
data class Coordinate(var x: Int, var y: Int) {

  /**
   * Returns if this coordinate is inside the bounds of the passed dimensions [w] and [h].
   */
  fun inBounds(w: Int, h: Int) = (x in 0 until w) && (y in 0 until h)
}