/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools

import com.stulsoft.file.tools.emptydir.ListEmptyDirsPanel
import com.stulsoft.file.tools.ext.ListAllExtensionsPanel
import com.stulsoft.file.tools.gps.GpsMapLauncher
import com.stulsoft.file.tools.keywords.KeywordAnalyzerPanel

import scala.swing.*
import scala.swing.Component.*
import scala.swing.event.*
object Main extends SimpleSwingApplication:
  override def top: Frame = new MainFrame{
    val version = ManifestInfo("com.stulsoft", "ys-file-tools").version() match
      case Some(version) =>
        version
      case None =>
        ""
    title = "YS File Tools " + version

    val listEmptyDirsPanel = new ListEmptyDirsPanel

    val listAllExtensionsPanel = new ListAllExtensionsPanel

    val keywordAnalyzerPanel = new KeywordAnalyzerPanel

    val panels: Array[Panel] = Array(listEmptyDirsPanel, listAllExtensionsPanel, keywordAnalyzerPanel)

    def activatePanel(activePanel: Panel): Unit =
      panels.foreach(thePanel => thePanel.visible = thePanel == activePanel)

    contents = new FlowPanel(FlowPanel.Alignment.Center)(panels: _*)

    menuBar = new MenuBar{
      contents += new Menu("Actions"){
        contents += new MenuItem(Action("List empty directories"){
          activatePanel(listEmptyDirsPanel)
        })

        contents += new MenuItem(Action("List all extensions"){
          activatePanel(listAllExtensionsPanel)
        })

        contents += new MenuItem(Action("Show on map"){
          GpsMapLauncher.showGps()
        })

        contents += new MenuItem(Action("Analyze keywords"){
          activatePanel(keywordAnalyzerPanel)
        })

        contents += new MenuItem(Action("Close") {
          dispose()
        })
      }
    }

    size = new Dimension(600,400)
    centerOnScreen()
  }

