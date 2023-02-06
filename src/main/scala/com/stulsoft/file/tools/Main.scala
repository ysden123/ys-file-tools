/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools

import com.stulsoft.file.tools.emptydir.ListEmptyDirsWindow
import com.stulsoft.file.tools.ext.ListAllExtensionsWindow
import com.stulsoft.file.tools.gps.GpsMapLauncher
import com.stulsoft.file.tools.keywords.KeywordAnalyzerWindow

import scala.swing.*
import scala.swing.Component.*
import scala.swing.event.*
object Main extends SimpleSwingApplication:
  override def top: Frame = new MainFrame{
    val version: String = ManifestInfo("com.stulsoft", "ys-file-tools").version() match
      case Some(version) =>
        version
      case None =>
        ""
    title = "YS File Tools " + version

    menuBar = new MenuBar{
      contents += new Menu("Actions"){
        contents += new MenuItem(Action("List empty directories"){
            new ListEmptyDirsWindow().open()
        })

        contents += new MenuItem(Action("List all extensions"){
            new ListAllExtensionsWindow().open()
        })

        contents += new MenuItem(Action("Show on map"){
          GpsMapLauncher.showGps()
        })

        contents += new MenuItem(Action("Analyze keywords"){
          new KeywordAnalyzerWindow().open()
        })

        contents += new MenuItem(Action("Close") {
          dispose()
        })
      }
    }

    size = new Dimension(600,400)
    centerOnScreen()
  }