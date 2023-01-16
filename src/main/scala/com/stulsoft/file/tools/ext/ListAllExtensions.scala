/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.ext

import org.apache.commons.io.FilenameUtils

import java.nio.file.{Files, NoSuchFileException, Paths}
import scala.collection.mutable
import scala.collection.mutable.SortedSet
import scala.io.StdIn.readLine
import scala.util.{Failure, Success, Using}

object ListAllExtensions:
  private def findAllExtensions(path: String): Unit =
    println(s"All file extensions in the $path:")
    val result = mutable.SortedSet[String]()

    Using(Files.walk(Paths.get(path))) {
      stream =>
        stream
          .map(path => FilenameUtils.getExtension(path.toString))
          .filter(extension => extension.nonEmpty)
          .forEach(extension => result += extension)
    } match
      case Success(_) =>
      case Failure(error) => error match
        case _: NoSuchFileException => println(s"""Cannot find the "$path" directory.""")
        case exception: Exception => println(exception.getMessage)
    result.foreach(println)
    println(s"Found ${result.size} extensions")

  def findAllExtensions(): Unit =
    println("Enter directory. Empty line to exist:")
    val path = readLine()
    if path.nonEmpty then findAllExtensions(path)
