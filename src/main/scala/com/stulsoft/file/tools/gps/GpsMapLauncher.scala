/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.gps

import scala.io.StdIn.readLine

import org.apache.commons.imaging.Imaging
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata

import java.awt.Desktop
import java.io.File
import java.net.URI

object GpsMapLauncher:
  private def extractGPSInfo(path: String): ExtractResult =
    try
      Imaging.getMetadata(new File(path)) match {
        case jpegImageMetadata: JpegImageMetadata => ExtractResult(Option(jpegImageMetadata.getExif.getGPS), None)
        case tiffImageMetadata: TiffImageMetadata => ExtractResult(Option(tiffImageMetadata.getGPS), None)
        case null => ExtractResult(Option.empty, Option("No GPS in the image"))
        case x => ExtractResult(Option.empty, Option(s"Unsupported type ${x.getClass.getName}"))
      }
    catch
      case exception: Exception => ExtractResult(Option.empty, Option(exception.getMessage))

  private def showOnMap(path: String): String =
    try
      extractGPSInfo(path) match
        case ExtractResult(Some(gpsInfo), None) =>
          val latitude = gpsInfo.getLatitudeAsDegreesNorth
          val longitude = gpsInfo.getLongitudeAsDegreesEast
          val searchURL = s"https://www.google.com/maps/search/?api=1&query=$latitude%2C$longitude"
          if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop.browse(new URI(searchURL))
            ""
          } else {
            "Cannot start browser"
          }
        case ExtractResult(None, Some(error)) => error
        case ExtractResult(_, _) => ""
    catch
      case exception: Exception => exception.getMessage

  def showOnMap(): Unit =
    println("Enter filename with path. Empty line to exist:")
    val path = readLine()
    if path.nonEmpty then println(showOnMap(path))

case class ExtractResult(gpsInfo: Option[TiffImageMetadata.GPSInfo], error: Option[String])