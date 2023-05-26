plugins {
  kotlin("jvm") version "1.8.21"
  application
}

group = "com.lucasalfare.flpathfinding"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://jitpack.io")
}

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

  implementation("com.lucasalfare.flengine:FLEngine") {
    version {
      branch = "master"
    }
  }

  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(11)
}

application {
  mainClass.set("MainKt")
}