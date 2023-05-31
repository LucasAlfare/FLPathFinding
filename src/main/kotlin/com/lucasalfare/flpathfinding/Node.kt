package com.lucasalfare.flpathfinding

import java.awt.geom.Point2D
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
//    g = abs(coordinate.x - start.x) + abs(coordinate.y - start.y)
//    h = abs(coordinate.x - end.x) + abs(coordinate.y - end.y)
//    f = g + h
    g = Point2D.distance(coordinate.x.toDouble(), coordinate.y.toDouble(), start.x.toDouble(), start.y.toDouble()).toInt()
    h = Point2D.distance(coordinate.x.toDouble(), coordinate.y.toDouble(), end.x.toDouble(), end.y.toDouble()).toInt()
    f = g + h
  }

  fun getNeighbors(): MutableList<Node> {
    val res = mutableListOf<Node>()

    val n = Coordinate(coordinate.x, coordinate.y + 1)
    val s = Coordinate(coordinate.x, coordinate.y - 1)
    val e = Coordinate(coordinate.x + 1, coordinate.y)
    val w = Coordinate(coordinate.x - 1, coordinate.y)

    val a = Coordinate(coordinate.x + 1, coordinate.y - 1)
    val b = Coordinate(coordinate.x - 1, coordinate.y - 1)
    val c = Coordinate(coordinate.x + 1, coordinate.y + 1)
    val d = Coordinate(coordinate.x - 1, coordinate.y + 1)

    if (n.inBounds(mapWidth, mapHeight)) res += Node(n)
    if (s.inBounds(mapWidth, mapHeight)) res += Node(s)
    if (e.inBounds(mapWidth, mapHeight)) res += Node(e)
    if (w.inBounds(mapWidth, mapHeight)) res += Node(w)

    if (a.inBounds(mapWidth, mapHeight)) res += Node(a)
    if (b.inBounds(mapWidth, mapHeight)) res += Node(b)
    if (c.inBounds(mapWidth, mapHeight)) res += Node(c)
    if (d.inBounds(mapWidth, mapHeight)) res += Node(d)

    res.forEach { it.parent = this }

    return res
  }
}