package com.lucasalfare.flpathfinding

import kotlinx.coroutines.delay


// TODO abstract these
const val mapWidth = 20
const val mapHeight = 20

/**
 * This functions performs A* algorithm based on a start end coordinates.
 *
 * This function also offers some callback functions that runs in particular
 * steps of the algorithm execution.
 * Normally, those callbacks are used to keep track of what is happening at some
 * point, making it possible to be used on UI explorers/visualizers or even to track the
 * algorithm execution and so on.
 */
suspend fun findPath(
  start: Coordinate,
  end: Coordinate,
  obstacles: List<Coordinate> = listOf(),
  delayTimeBetweenSetps: Long = -1,
  onAlgorithmInitied: () -> Unit = {},
  onUpdateCurrentExploringNode: (Node) -> Unit = {},
  onOpenListInsertion: (Node) -> Unit = {},
  onOpenListRemove: (Node) -> Unit = {},
  onOpenListSort: (List<Node>) -> Unit = {},
  onClosedListInsertion: (Node) -> Unit = {},
  onNodeProcessed: (Node) -> Unit = {},
  onDone: (Node, Boolean) -> Unit = { _, _ -> },
  onTogglePause: () -> Boolean = { false },
  onBreakRequest: () -> Boolean = { false }
): Node {
  suspend fun doDelay() {
    if (delayTimeBetweenSetps > 0)
      delay(delayTimeBetweenSetps)
  }

  val open = mutableListOf<Node>()
  val closed = mutableListOf<Node>()

  // by default, the first node to be explored is the node with
  // coords of the start
  var currExploringNode = Node(start)
  onAlgorithmInitied()
  onUpdateCurrentExploringNode(currExploringNode)
  doDelay()

  // We always insert the initial node in the open list. This
  // will make it the first node to be explored.
  open += currExploringNode
  onOpenListInsertion(currExploringNode)
  doDelay()

  while (true) {
    println(onBreakRequest())
    if (onBreakRequest()) {
      onDone(currExploringNode, false)
      break
    }

    println(onTogglePause())
    if (!onTogglePause()) {
      // if the current exploring node matches the coord of the targeted end
      // then pathfinding was finished
      if (currExploringNode.coordinate.x == end.x && currExploringNode.coordinate.y == end.y) {
        onDone(currExploringNode, true)
        doDelay()
        break
      }

      // if we already explored the max number of "tiles" on this "map",
      // then we don't have solutions, then just finishes the pathfinding
      if (closed.size == mapWidth * mapHeight) {
        onDone(currExploringNode, false)
        doDelay()
      }

      // always get the neighbors of the current exploring node
      val neighbors = currExploringNode.getNeighbors()
      neighbors.forEach { neighbor ->
        // for each neighbor, we check if its coords are coords of obstacles,
        // then update its value based on this
        if (obstacles.any { it.x == neighbor.coordinate.x && it.y == neighbor.coordinate.y }) {
          neighbor.blocked = true
        }

        if (!neighbor.blocked) {
          // for each non-obstacle neighbor, we check if it exists in the
          // closed or in the open lists
          if (!open.containsNode(neighbor) && !closed.containsNode(neighbor)) {
            // if the neighbor doesn't exist in those lists,
            // then process their costs
            neighbor.processCosts(start, end)
            onNodeProcessed(neighbor)
            doDelay()

            // after processing, adds it to the open list
            // to be explored later (if needed in that time)
            open += neighbor
            onOpenListInsertion(neighbor)
            doDelay()
          }
        }
      }

      // as we explored all neighbors of the current node, then this
      // node can go to the closed list
      closed += currExploringNode
      onClosedListInsertion(currExploringNode)
      doDelay()

      // as the current node is in the closed list, we can now remove it
      // from the open list
      open -= currExploringNode
      onOpenListRemove(currExploringNode)
      doDelay()

      // now we sort the open list based on the F cost of its nodes
      open.sortBy { it.f }
      onOpenListSort(open)
      doDelay()

      // finnally, we just update the current node to be the first node
      // of the sorted list, then, in the next loop, it (and its neighbors)
      // will be explored
      currExploringNode = open.first()
      onUpdateCurrentExploringNode(currExploringNode)
      doDelay()
    }
  }

  return currExploringNode
}

/**
 * Directly extends a list with [Node] items.
 */
internal fun List<Node>.containsNode(node: Node) =
  this.any { it.coordinate.x == node.coordinate.x && it.coordinate.y == node.coordinate.y }

/**
 * Just to debug.
 */
internal fun printPathfindingResult(
  start: Coordinate,
  end: Coordinate,
  lastNode: Node,
  obstacles: List<Coordinate> = listOf()
) {
  val empty = "_"
  val waypoint = "."
  val blocked = "#"

  val table = Array(mapHeight) { Array(mapWidth) { empty } }
  var tmp = lastNode
  while (tmp.parent != null) {
    table[tmp.coordinate.y][tmp.coordinate.x] = waypoint
    tmp = tmp.parent!!
  }

  table[start.y][start.x] = "!"
  table[end.y][end.x] = "X"

  obstacles.forEach {
    table[it.y][it.x] = blocked
  }

  table.forEach { a ->
    a.forEach { b ->
      print("$b  ")
    }
    println()
  }
}
