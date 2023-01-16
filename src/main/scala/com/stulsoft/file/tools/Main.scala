/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools

import com.stulsoft.file.tools.emptydir.ListEmptyDirs
import com.stulsoft.file.tools.ext.ListAllExtensions
import com.stulsoft.file.tools.gps.GpsMapLauncher
import com.stulsoft.file.tools.menu.{Menu, MenuItem}
import com.typesafe.scalalogging.StrictLogging

object Main extends StrictLogging:
  def main(args: Array[String]): Unit =
    ManifestInfo("com.stulsoft", "ys-file-tools").showManifest()
    Menu(
      Set(
        MenuItem("List empty directories", Option(ListEmptyDirs.listEmptyDirs)),
        MenuItem("List all extensions", Option(ListAllExtensions.findAllExtensions)),
        MenuItem("Show on map", Option(GpsMapLauncher.showOnMap))
      )
    ).showMenu().action.foreach(action => action())

