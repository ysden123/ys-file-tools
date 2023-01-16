/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.emptydir

object RunEmptyDirService:
  def main(args: Array[String]): Unit =
    ListEmptyDirs.listEmptyDirs()
