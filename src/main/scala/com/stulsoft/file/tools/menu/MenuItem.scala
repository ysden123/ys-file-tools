/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.menu

case class MenuItem(name:String, action: Option[() => Unit] = None)

object MenuItem:
  val ExitMenuItem = MenuItem("Exit")
