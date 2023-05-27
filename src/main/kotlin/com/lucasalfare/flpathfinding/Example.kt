package com.lucasalfare.flpathfinding

private var start = Coord(0, 3)
private var end = Coord(3, 4)
private val obstacles = mutableListOf(
  Coord(2, 2),
  Coord(2, 3),
  Coord(2, 4),
  Coord(1, 2),
  Coord(3, 1),
  Coord(3, 2)
)

suspend fun main() {
  findPath(
    start, end, obstacles,
    onDone = { lastNode, successfulFound ->
      if (successfulFound) {
        println("The path was successful found. This is the result:")
        printPathfindingResult(start, end, lastNode, obstacles)
      }
    }
  )
}

