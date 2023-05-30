package com.lucasalfare.flpathfinding

private var start = Coordinate(0, 3)
private var end = Coordinate(3, 4)
private val obstacles = mutableListOf(
  Coordinate(2, 2),
  Coordinate(2, 3),
  Coordinate(2, 4),
  Coordinate(1, 2),
  Coordinate(3, 1),
  Coordinate(3, 2)
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

