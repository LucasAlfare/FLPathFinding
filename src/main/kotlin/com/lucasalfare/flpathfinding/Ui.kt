package com.lucasalfare.flpathfinding

import com.lucasalfare.flengine.App
import com.lucasalfare.flengine.graphics.Renderer
import com.lucasalfare.flengine.input.Input
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import java.awt.event.KeyEvent

var start = Coord(0, 3)
var end = Coord(3, 4)
val obstacles = listOf(
  Coord(2, 2),
  Coord(2, 3),
  Coord(2, 4),
  Coord(1, 2),
  Coord(3, 1),
  Coord(3, 2)
)

class ExampleApp : App("Example") {

  override fun init() {
    doPathFinding()
  }

  override fun update(vararg args: Any) {
    val ts = args[0] as Float

    if (Input.isKeyUp(KeyEvent.VK_SPACE)) {

    }
  }

  private var g: Graphics2D? = null
  override fun render(r: Renderer) {
    if (g == null) g = r.createGraphics()

  }

  private val tmpJob = Job()
  private fun doPathFinding() {
    CoroutineScope(tmpJob).launch {
      findPath(
        start,
        end,
        obstacles,
        onDone = { lastNode -> }
      )
    }
  }
}