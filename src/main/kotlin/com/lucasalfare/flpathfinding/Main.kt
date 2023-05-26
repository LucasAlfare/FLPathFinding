package com.lucasalfare.flpathfinding

import com.lucasalfare.flengine.graphics.Engine
import com.lucasalfare.flengine.graphics.ScreenHeight
import com.lucasalfare.flengine.graphics.ScreenWidth


const val width = 20
const val height = 20
const val scaleFactor = 100f

suspend fun main() {
  ScreenWidth = 5 * scaleFactor
  ScreenHeight = 5 * scaleFactor
  val e = Engine(ExampleApp())
  e.setSize(ScreenWidth.toInt(), ScreenHeight.toInt())
  e.start()
}

