/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.emptydir

import java.nio.file.{Files, NoSuchFileException, Path, Paths}
import scala.io.StdIn.readLine
import scala.jdk.CollectionConverters.*
import scala.util.{Failure, Success, Using}

object ListEmptyDirs:
  private def predicateEmptyDir(p: Path): Boolean =
    Using(Files.list(p)) {
      stream => stream.findAny().isEmpty
    } match
      case Success(value) => value
      case Failure(exception) =>
        exception.printStackTrace()
        false

  private def listEmptyDirs(path: String): Unit =
    println(s"Empty directories in the $path:")
    Using(Files.walk(Paths.get(path))) {
      stream =>
        var count = 0
        stream.filter(f => Files.isDirectory(f))
          .filter(p => predicateEmptyDir(p))
          .map(p => p.toFile.getAbsolutePath)
          .forEach(dir => {
            count += 1
            println(dir)
          })
        println(s"Found $count empty directories")
    } match
      case Success(_) =>
      case Failure(error) => error match
        case _: NoSuchFileException => println(s"""Cannot find the "$path" directory.""")
        case exception: Exception => println(exception.getMessage)

  def listEmptyDirs(): Unit =
    println("Enter directory. Empty line to exist:")
    val path = readLine()
    if path.nonEmpty then listEmptyDirs(path)


