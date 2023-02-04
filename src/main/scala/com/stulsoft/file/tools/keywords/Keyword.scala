/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.keywords

case class Keyword(name: String, level:Int, parent: Option[Keyword] = Option.empty):
  private var children: Seq[Keyword] = List[Keyword]()

  override def toString: String =
    val parentInfo = if parent.isEmpty then
      ""
    else
      parent.get.name
    s"Keyword: {name:$name, level=$level, children: $children, parent: $parentInfo}"

  def listChildren(): Seq[Keyword] = children

  def addChild(keyword: Keyword): Keyword =
    children = children.appended(keyword)
    this


