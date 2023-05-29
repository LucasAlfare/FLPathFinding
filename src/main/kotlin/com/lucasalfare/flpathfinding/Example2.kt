package com.lucasalfare.flpathfinding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.JFrame

private var start = Coord(0, 3)
private var end = Coord(3, 4)
private val obstacles = listOf(
  Coord(2, 2),
  Coord(2, 3),
  Coord(2, 4),
  Coord(1, 2),
  Coord(3, 1),
  Coord(3, 2)
)

class Window : JFrame() {

  private val map = Map()

  init {
    setSize(800, 800)
    setLocationRelativeTo(null)
    defaultCloseOperation = 3
    layout = FlowLayout()
    this.addKeyListener(map.keyAdapter)
    this.add(map)
    isVisible = true
  }
}

class Map : JComponent() {

  private val cellSize = 20

  private val state = mutableListOf<MutableList<String>>()

  private val emptyFlag = "_"
  private val waypointFlag = "."
  private val startFlag = "!"
  private val endFlag = "X"
  private val obstacleFlag = "#"
  private val processedNodeFlag = "ok"

  private var currClickX = 0
  private var currClickY = 0

  private var pausePathfinding = false
  private var breakPathfinding = false

  private val auxJob = Job()

  private val mouseAdapter = object : MouseAdapter() {
    override fun mouseReleased(e: MouseEvent) {
      if (e.button == MouseEvent.BUTTON1) {
        currClickX = (e.x) / cellSize
        currClickY = (e.y) / cellSize
        println("grid: $currClickX,$currClickY")

        start.x = end.x
        start.y = end.y

        end.x = currClickX
        end.y = currClickY

        doPathfinding()
      }
    }
  }

  val keyAdapter = object : KeyAdapter() {
    override fun keyReleased(e: KeyEvent) {
      if (e.keyCode == KeyEvent.VK_SPACE) {
        pausePathfinding = !pausePathfinding
      }

      if (e.keyCode == KeyEvent.VK_ESCAPE) {
        breakPathfinding = true
      }
    }
  }

  init {
    preferredSize = Dimension(mapWidth * cellSize, mapHeight * cellSize)
    this.addMouseListener(mouseAdapter)
    this.addKeyListener(keyAdapter)
    doPathfinding()
  }

  override fun paintComponent(g: Graphics) {
    val g2 = g as Graphics2D

    state.forEachIndexed { y, row ->
      row.forEachIndexed { x, flag ->
        g2.color = when (flag) {
          waypointFlag -> Color.BLUE
          startFlag -> Color.YELLOW
          endFlag -> Color.GREEN
          obstacleFlag -> Color.DARK_GRAY
          processedNodeFlag -> Color.LIGHT_GRAY
          else -> Color.WHITE
        }

        g2.fillRect(x * cellSize, y * cellSize, cellSize, cellSize)

        g2.color = Color.BLACK
        g2.drawRect(x * cellSize, y * cellSize, cellSize, cellSize)
      }
    }

    g2.color = Color.RED
    g2.drawRect(0, 0, preferredSize.width - 1, preferredSize.height - 1)
  }

  private fun resetState() {
    state.clear()
    repeat(mapHeight) {
      val tmp = mutableListOf<String>()
      repeat(mapWidth) {
        tmp += emptyFlag
      }
      state += tmp
    }
  }

  private fun doPathfinding() {
    resetState()

    CoroutineScope(auxJob).launch {
      findPath(
        start, end,
        obstacles,
        delayTimeBetweenSetps = 7,
        onNodeProcessed = {
          state[start.y][start.x] = startFlag
          repaint()
        },
        onAlgorithmInitied = {
          obstacles.forEach {
            state[it.y][it.x] = obstacleFlag
          }
        },
        onOpenListSort = {
          val first = it.first()
          state[first.coord.y][first.coord.x] = endFlag
        },
        onClosedListInsertion = {
          state[it.coord.y][it.coord.x] = processedNodeFlag
        },
        onDone = { node, pathSuccessfulFound ->
          if (pathSuccessfulFound) {
            var tmp = node
            while (tmp.parent != null) {
              state[tmp.coord.y][tmp.coord.x] = waypointFlag
              tmp = tmp.parent!!
            }

            state[start.y][start.x] = startFlag
            state[end.y][end.x] = endFlag
          } else {
            resetState()
            state[start.y][start.x] = startFlag
            obstacles.forEach {
              state[it.y][it.x] = obstacleFlag
            }
          }

          repaint()
        },
        onTogglePause = { pausePathfinding },
        onBreakRequest = { breakPathfinding }
      )
    }
  }
}

fun main() {
  Window()
}