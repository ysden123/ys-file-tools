/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.menu

import scala.io.StdIn.readInt
import scala.util.{Failure, Success, Try}

case class Menu(menuItems: Set[MenuItem]):
  def showMenu(): MenuItem =
    val itemsSet = Set(MenuItem.ExitMenuItem) ++ menuItems
    val withIndex = itemsSet.zipWithIndex
    var selectedItem: Option[MenuItem] = None

    while
      selectedItem.isEmpty
    do {
      println(s"Choose item (0 - ${itemsSet.size - 1})")
      withIndex.foreach(item => println(s"${item._2} - ${item._1.name}"))
      try
        val choice = readInt()
        selectedItem = withIndex.find((_, index) => index == choice) match
          case Some(value) => Some(value._1)
          case None =>
            println("Wrong answer")
            None
      catch
        case exception: Exception =>
          println(s"Wrong answer: ${exception.getMessage}")
    }

    selectedItem.get


