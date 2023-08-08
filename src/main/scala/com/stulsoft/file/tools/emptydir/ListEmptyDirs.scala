/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.emptydir

import java.nio.file.{Files, NoSuchFileException, Path, Paths}
import scala.concurrent.{ExecutionContext, Future, Promise}
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
    given ExecutionContext = ExecutionContext.global

    val promise = Promise[String]()
    Future {
      Using(Files.walk(Paths.get(path))) {
        stream =>
          val listOfEmptyDirs = stream.filter(f => Files.isDirectory(f))
            .filter(p => predicateEmptyDir(p))
            .map(p => p.toFile.getAbsolutePath)
            .toList
            .asScala
          if listOfEmptyDirs.isEmpty then
            "No empty directory found\n"
          else
            listOfEmptyDirs.mkString("\n")
      } match
        case Success(result) => promise.success(result)
        case Failure(error) => error match
          case _: NoSuchFileException => promise.success(s"""Cannot find the "$path" directory.""")
          case exception: Exception => promise.success(exception.getMessage)
    }

    promise.future


