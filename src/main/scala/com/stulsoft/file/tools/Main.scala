/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools

import com.stulsoft.common.ManifestInfo
import com.stulsoft.file.tools.emptydir.ListEmptyDirsFrame
import com.stulsoft.file.tools.ext.ListAllExtensionsFrame
import com.stulsoft.file.tools.gps.GpsMapLauncher
import com.stulsoft.file.tools.keywords.KeywordAnalyzerFrame

import scala.swing.*

object Main extends SimpleSwingApplication:
  override def top: Frame = new MainFrame {
    val mainFrame: MainFrame = this
    title = ManifestInfo("com.stulsoft", "ys-file-tools").buildTitle("YS File Tools")
    menuBar = new MenuBar {
      contents += new Menu("Actions") {
        contents += new MenuItem(Action("List empty directories") {
          mainFrame.contents = new ListEmptyDirsFrame
        })

        contents += new MenuItem(Action("List all extensions") {
          mainFrame.contents = new ListAllExtensionsFrame
        })

        contents += new MenuItem(Action("Show on map") {
          GpsMapLauncher.showGps()
        })

        contents += new MenuItem(Action("Analyze keywords") {
          mainFrame.contents = new KeywordAnalyzerFrame
        })

        contents += new MenuItem(Action("Close") {
          dispose()
        })
      }
    }

    size = new Dimension(600, 400)
    centerOnScreen()
  }