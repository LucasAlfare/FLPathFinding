package com.lucasalfare.flpathfinding

import kotlin.math.abs

/**
 * Represents each point inside a map graph.
 */
data class Node(val coordinate: Coordinate) {
  var parent: Node? = null
  var f: Int = 0
  var blocked = false

  private var g: Int = 0
  private var h: Int = 0

  fun processCosts(start: Coordinate, end: Coordinate) {
    // not considering diagonals yet
    g = abs(coordinate.x - start.x) + abs(coordinate.y - start.y)
    h = abs(coordinate.x - end.x) + abs(coordinate.y - end.y)
    f = g + h
  }

  fun getNeighbors(): MutableList<Node> {
    val res = mutableListOf<Node>()

    // not considering diagonals yet
    val n = Coordinate(coordinate.x, coordinate.y + 1)
    val s = Coordinate(coordinate.x, coordinate.y - 1)
    val e = Coordinate(coordinate.x + 1, coordinate.y)
    val w = Coordinate(coordinate.x - 1, coordinate.y)

    if (n.inBounds(mapWidth, mapHeight)) res += Node(n)
    if (s.inBounds(mapWidth, mapHeight)) res += Node(s)
    if (e.inBounds(mapWidth, mapHeight)) res += Node(e)
    if (w.inBounds(mapWidth, mapHeight)) res += Node(w)

    res.forEach { it.parent = this }

    return res
  }
}