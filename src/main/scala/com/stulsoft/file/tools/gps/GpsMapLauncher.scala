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
import javax.swing.filechooser.FileFilter
import scala.swing.Dialog.showMessage
import scala.swing.FileChooser
import scala.swing.FileChooser.Result.Approve

object GpsMapLauncher:
  private var initFile: File = _

  private def extractGPSInfo(path: String): ExtractResult =
    try
      Imaging.getMetadata(new File(path)) match {
        case jpegImageMetadata: JpegImageMetadata =>
          val gps = jpegImageMetadata.getExif.getGPS
          if gps == null then
            ExtractResult(Option.empty, Option("No GPS information in the image"))
          else
            ExtractResult(Option(gps), None)
        case tiffImageMetadata: TiffImageMetadata =>
          val gps = tiffImageMetadata.getGPS
          if gps == null then
            ExtractResult(Option.empty, Option("No GPS information in the image"))
          else
            ExtractResult(Option(gps), None)
        case null => ExtractResult(Option.empty, Option("No GPS in the image"))
        case x => ExtractResult(Option.empty, Option(s"Unsupported type ${x.getClass.getName}"))
      }
    catch
      case exception: Exception => ExtractResult(Option.empty, Option(exception.getMessage))

  private def showOnMap(path: String): Option[String] =
    try
      extractGPSInfo(path) match
        case ExtractResult(Some(gpsInfo), None) =>
          val latitude = gpsInfo.getLatitudeAsDegreesNorth
          val longitude = gpsInfo.getLongitudeAsDegreesEast
          val searchURL = s"https://www.google.com/maps/search/?api=1&query=$latitude%2C$longitude"
          println(s"searchURL: $searchURL")
          if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop.browse(new URI(searchURL))
            Option.empty
          } else {
            Option("Cannot start browser")
          }
        case ExtractResult(None, Some(error)) => Option(error)
        case ExtractResult(_, _) => Option.empty
    catch
      case exception: Exception => Option(exception.getMessage)

  def showGps(): Unit =
    val chooser = new FileChooser(initFile)
    chooser.title = "Select image to show on Google map"
    chooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
    chooser.fileFilter = new FileFilter {
      override def accept(f: File): Boolean = {
        if f.isFile then
          val extension = f.getPath.toUpperCase()
          extension.endsWith(".JPG")
            || extension.endsWith(".PNG")
            || extension.endsWith(".TIF")
        else
          true
      }

      override def getDescription: String = "Image files (.jpg, .png, .tif)"
    }

    val choice = chooser.showOpenDialog(null)
    if choice == Approve then
      val file = chooser.selectedFile
      initFile = file
      showOnMap(file.getAbsolutePath) match
        case Some(error) =>
          showMessage(parent = null, error)
        case _ =>

case class ExtractResult(gpsInfo: Option[TiffImageMetadata.GPSInfo], error: Option[String])