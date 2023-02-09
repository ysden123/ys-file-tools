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
  case class ExtensionInfo(name: String, count: Int)

  def findAllExtensions(path: String): Either[String, String] =
    Using(Files.walk(Paths.get(path))) {
      val extensionInfoMap = new mutable.HashMap[String, ExtensionInfo]
      stream =>
        stream
          .map(path =>
            FilenameUtils.getExtension(path.toString))
          .filter(extension => extension.nonEmpty)
          .forEach(extension => {
            val extensionInfo = extensionInfoMap.getOrElseUpdate(extension, ExtensionInfo(extension, 0))
            extensionInfoMap.put(extension, ExtensionInfo(extension, extensionInfo.count + 1))
          })
        extensionInfoMap
          .values
          .toList
          .sortBy(_.count)
          .reverse
    } match
      case Success(extensionInfoList) =>
        val total = extensionInfoList.map(_.count).sum
        val result = extensionInfoList
          .map(extensionInfo => f"${extensionInfo.name}%s\t${extensionInfo.count}%d\t${1.0 * extensionInfo.count * 100 / total}%2.2f%%")
          .mkString("\n")
        Right(result)
      case Failure(error) => error match
        case _: NoSuchFileException => Left(s"""Cannot find the "$path" directory.""")
        case exception: Exception => Left(exception.getMessage)