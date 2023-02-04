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
  def findAllExtensions(path: String): Either[String, String] =
    val result = mutable.SortedSet[String]()

    Using(Files.walk(Paths.get(path))) {
      stream =>
        stream
          .map(path => FilenameUtils.getExtension(path.toString))
          .filter(extension => extension.nonEmpty)
          .forEach(extension => result += extension)
    } match
      case Success(_) =>
        Right(result.mkString("\n"))
      case Failure(error) => error match
        case _: NoSuchFileException => Left(s"""Cannot find the "$path" directory.""")
        case exception: Exception => Left(exception.getMessage)
