package com.lucasalfare.flpathfinding

import kotlinx.coroutines.delay
import kotlin.math.abs


// TODO abstract these
const val mapWidth = 20
const val mapHeight = 20

/**
 * This functions performs A* algorithm based on a start end coordinates.
 *
 * This function also offers some callback functions that runs in particular
 * steps of the algorithm execution.
 * Normally, those callbacks are used to keep track of what is happening at some
 * point, making it possible to be used on UI explorers/visualizers.
 */
suspend fun findPath(
  start: Coord,
  end: Coord,
  obstacles: List<Coord> = listOf(),
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
      if (currExploringNode.coord.x == end.x && currExploringNode.coord.y == end.y) {
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
        if (obstacles.any { it.x == neighbor.coord.x && it.y == neighbor.coord.y }) {
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
  this.any { it.coord.x == node.coord.x && it.coord.y == node.coord.y }

/**
 * Represents a bidimensional coordinate inside a space.
 */
data class Coord(var x: Int, var y: Int) {

  fun inBounds(w: Int, h: Int) =
    (x in 0 until w) && (y in 0 until h)
}

/**
 * Represents each point inside a map graph.
 */
data class Node(val coord: Coord) {
  var parent: Node? = null
  var f: Int = 0
  var blocked = false

  private var g: Int = 0
  private var h: Int = 0

  fun processCosts(start: Coord, end: Coord) {
    // not considering diagonals
    g = abs(coord.x - start.x) + abs(coord.y - start.y)
    h = abs(end.x - start.x) + abs(end.y - start.y)
    f = g + h
  }

  fun getNeighbors(): MutableList<Node> {
    val res = mutableListOf<Node>()

    // not considering diagonals
    val n = Coord(coord.x, coord.y + 1)
    val s = Coord(coord.x, coord.y - 1)
    val e = Coord(coord.x + 1, coord.y)
    val w = Coord(coord.x - 1, coord.y)

    if (n.inBounds(mapWidth, mapHeight)) res += Node(n)
    if (s.inBounds(mapWidth, mapHeight)) res += Node(s)
    if (e.inBounds(mapWidth, mapHeight)) res += Node(e)
    if (w.inBounds(mapWidth, mapHeight)) res += Node(w)

    res.forEach { it.parent = this }

    return res
  }
}


/**
 * Just to debug.
 */
internal fun printPathfindingResult(
  start: Coord,
  end: Coord,
  lastNode: Node,
  obstacles: List<Coord> = listOf()
) {
  val empty = "_"
  val waypoint = "."
  val blocked = "#"

  val table = Array(mapHeight) { Array(mapWidth) { empty } }
  var tmp = lastNode
  while (tmp.parent != null) {
    table[tmp.coord.y][tmp.coord.x] = waypoint
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