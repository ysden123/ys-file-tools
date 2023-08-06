/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.keywords

import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object KeywordAnalyzer extends LazyLogging:
  private class CurrentStatus:
    var level: Int = 1
    var lastKeyword: Keyword = _
    var parentKeyword: Option[Keyword] = Option.empty
  end CurrentStatus


  case class KeywordStatistics(rootKeywordCount: Int, maxLevel: Int)

  def buildKeywordList(filename: String): Iterable[Keyword] =
    val currentStatus = new CurrentStatus

    val keywordList = ListBuffer[Keyword]()

    try
      val source = Source.fromFile(filename)("UTF-8")
      for (line <- source.getLines()) {
        if line.nonEmpty then
          val extractionResult = extractKeyWord(line)
          val newKeywordLevel = extractionResult._1
          if newKeywordLevel == currentStatus.level then
            // Same level
            val newKeyword = Keyword(extractionResult._2, newKeywordLevel, currentStatus.parentKeyword)
            keywordList += newKeyword

            // Correct current status
            currentStatus.lastKeyword = newKeyword

          else if newKeywordLevel > currentStatus.level then
            // Up one level
            val newKeyword = Keyword(extractionResult._2, newKeywordLevel, Option(currentStatus.lastKeyword))
            currentStatus.lastKeyword.addChild(newKeyword)

            // Correct current status
            currentStatus.level = newKeywordLevel
            currentStatus.parentKeyword = Option(currentStatus.lastKeyword)
            currentStatus.lastKeyword = newKeyword
          else
            // Down one or more levels
            val downLevels = currentStatus.level - newKeywordLevel
            for (_ <- 1 to downLevels) {
              if currentStatus.parentKeyword.nonEmpty then
                currentStatus.parentKeyword = currentStatus.parentKeyword.get.parent
              else
                currentStatus.parentKeyword = Option.empty
            }
            val newKeyword = Keyword(extractionResult._2, newKeywordLevel, currentStatus.parentKeyword)
            if currentStatus.parentKeyword.isEmpty then
              keywordList += newKeyword
            else
              currentStatus.parentKeyword.get.addChild(newKeyword)

            // Correct current status
            currentStatus.lastKeyword = newKeyword
            currentStatus.level = newKeywordLevel
      }
      source.close()
    catch
      case exception: Exception =>
        println(s"currentParentKeyword: ${currentStatus.parentKeyword}, currentKeywordLevel = ${currentStatus.level}")
        logger.error(exception.getMessage, exception)

    keywordList.toList
  end buildKeywordList

  def buildKeywordStatistics(keywords: Iterable[Keyword]): KeywordStatistics =
    val maxLevel = if keywords.isEmpty then 0 else findMaxLevel(1, keywords)
    KeywordStatistics(keywords.size, maxLevel)

  private def extractKeyWord(line: String): (Int, String) =
    val splitResult = line.split("\\t")
    (splitResult.length, splitResult(splitResult.length - 1))

  def walk(keywords: Iterable[Keyword], f: Keyword => Unit): Unit =
    keywords.foreach(keyword =>
      f(keyword)
      val children = keyword.listChildren()
      if children.nonEmpty then
        walk(children, f)
    )

  def findDuplicates(keywords: Iterable[Keyword], ignoreCase: Boolean): Map[String, Iterable[Keyword]] =
    val duplicates = new mutable.TreeMap[String, ListBuffer[Keyword]]()
    walk(keywords, keyword =>
      val list = if !duplicates.contains(keyword.name) then
        val newList = ListBuffer[Keyword]()
        if ignoreCase then
          duplicates.addOne(keyword.name.toLowerCase(), newList)
        else
          duplicates.addOne(keyword.name, newList)
        newList
      else
        duplicates(keyword.name)

      list.addOne(keyword)
    )

    duplicates
      .filter(keyword => keyword._2.size > 1)
      .map(e => e._1 -> e._2.toList)
      .toMap

  def findByName(keywords: Iterable[Keyword], name: String, ignoreCase: Boolean): Iterable[Keyword] =
    val result = new ListBuffer[Keyword]
    walk(keywords, keyword =>
      if (ignoreCase) {
        if keyword.name.equalsIgnoreCase(name) then
          result += keyword
      } else {
        if keyword.name == name then
          result += keyword
      }
    )
    result.toList

  private def findMaxLevel(initialMaxLevel: Int, keywords: Iterable[Keyword]): Int =
    var maxLevel = initialMaxLevel
    keywords.foreach(keyword =>
      if keyword.level > maxLevel then
        maxLevel = keyword.level

      val children = keyword.listChildren()
      if children.nonEmpty then
        maxLevel = Math.max(maxLevel, findMaxLevel(maxLevel, keyword.listChildren()))
    )
    maxLevel

  def outputAllKeywords(keywords: Iterable[Keyword]): Unit =
    walk(keywords, keyword =>
      for (_ <- 1 until keyword.level)
        print("\t")

      println(keyword.name)
    )

end KeywordAnalyzer

