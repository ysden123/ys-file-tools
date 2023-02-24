/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.emptydir

import java.nio.file.{Files, NoSuchFileException, Path, Paths}
import scala.concurrent.{Future, Promise}
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

  def buildListOfEmptyDirs(path: String): Future[String] =
    val promise = Promise[String]()
    Using(Files.walk(Paths.get(path))) {
      stream =>
        stream.filter(f => Files.isDirectory(f))
          .filter(p => predicateEmptyDir(p))
          .map(p => p.toFile.getAbsolutePath)
          .toList
          .asScala
          .mkString("\n")
    } match
      case Success(result) => promise.success(result)
      case Failure(error) => error match
        case _: NoSuchFileException => promise.success(s"""Cannot find the "$path" directory.""")
        case exception: Exception => promise.success(exception.getMessage)

    promise.future


