/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.keywords

import org.scalatest.funsuite.AnyFunSuite

class KeywordAnalyzerTest extends AnyFunSuite:
  def outputStructure(keyword: Keyword): Unit =
    println(s"""name: ${keyword.name}, level=${keyword.level}, parent: ${if keyword.parent.nonEmpty then keyword.parent.get.name else ""}""")

  test("build keyword list statistics") {
    var keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test1.txt")
    var keywordStatistics = KeywordAnalyzer.buildKeywordStatistics(keywords)
    assert(5 == keywordStatistics.rootKeywordCount)

    keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test2.txt")
    keywordStatistics = KeywordAnalyzer.buildKeywordStatistics(keywords)
    assert(4 == keywordStatistics.rootKeywordCount)

    keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test3.txt")
    keywordStatistics = KeywordAnalyzer.buildKeywordStatistics(keywords)
    assert(2 == keywordStatistics.rootKeywordCount)
  }

  test("structure test11"){
    val keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test11.txt")
/*
    println("Structure:")
    KeywordAnalyzer.walk(keywords, outputStructure)
*/
    val findByNameResult = KeywordAnalyzer.findByName(keywords, "key32", true)
    assert(findByNameResult.size == 1)
    assert(findByNameResult.head.name == "key32")
    assert(findByNameResult.head.parent.get.name == "key3")
  }

  test("structure test1"){
    val keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test1.txt")
/*
    println("Structure:")
    KeywordAnalyzer.walk(keywords, outputStructure)
*/
    var findByNameResult = KeywordAnalyzer.findByName(keywords, "key32", true)
    assert(findByNameResult.size == 1)
    assert(findByNameResult.head.name == "key32")
    assert(findByNameResult.head.parent.get.name == "key3")

    findByNameResult = KeywordAnalyzer.findByName(keywords, "key33", true)
    assert(findByNameResult.size == 1)
    assert(findByNameResult.head.name == "key33")
    assert(findByNameResult.head.parent.get.name == "key3")
  }

  test("check statistics 2") {
    var keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test1.txt")
    var keywordStatistics = KeywordAnalyzer.buildKeywordStatistics(keywords)
    assert(4 == keywordStatistics.maxLevel)
    KeywordAnalyzer.outputAllKeywords(keywords)

    keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test2.txt")
    keywordStatistics = KeywordAnalyzer.buildKeywordStatistics(keywords)
    assert(3 == keywordStatistics.maxLevel)

    keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test3.txt")
    keywordStatistics = KeywordAnalyzer.buildKeywordStatistics(keywords)
    assert(3 == keywordStatistics.maxLevel)
  }

  test("duplicates") {
    var keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test1.txt")
    var duplicates = KeywordAnalyzer.findDuplicates(keywords, false)
    assert(duplicates.isEmpty)

    keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test4.txt")
    duplicates = KeywordAnalyzer.findDuplicates(keywords, false)
    assert(duplicates.nonEmpty)
    assert(duplicates.size == 1)
    duplicates.foreach(d => println(s"${d._1} -> ${d._2}"))

    keywords = KeywordAnalyzer.buildKeywordList("C:/work/Lightroom Keywords.txt")
    duplicates = KeywordAnalyzer.findDuplicates(keywords, false)
    println(s"Number of duplicates = ${duplicates.size}")
    duplicates.foreach(d => println(s"${d._1} -> ${d._2}"))
  }

  test("duplicates 2") {
    def output(keyword: Keyword): Unit =
      println(s"""name: ${keyword.name}, level=${keyword.level}, parent: ${if keyword.parent.nonEmpty then keyword.parent.get.name else ""}""")

    val keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test5.txt")
    val duplicates:Map[String,Iterable[Keyword]] = KeywordAnalyzer.findDuplicates(keywords, false)
    println(s"Number of duplicates = ${duplicates.size}")
    duplicates.foreach(duplicate =>
      val name = duplicate._1
      val list = duplicate._2
      println(s"duplicated name: $name")
      list.foreach(output)
    )
  }

  test("structure"){
    def output(keyword: Keyword): Unit =
      println(s"""name: ${keyword.name}, level=${keyword.level}, parent: ${if keyword.parent.nonEmpty then keyword.parent.get.name else ""}""")

//    val keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test5.txt")
//    val keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test6.txt")
//    val keywords = KeywordAnalyzer.buildKeywordList2("src/test/resources/keywords/test6.txt")
//    val keywords = KeywordAnalyzer.buildKeywordList("src/test/resources/keywords/test5.txt")
    val keywords = KeywordAnalyzer.buildKeywordList("C:/work/Lightroom Keywords.txt")
    println("Structure:")
    KeywordAnalyzer.walk(keywords, output)
  }

  test("duplicates real") {
    def output(keyword: Keyword): Unit =
      println(s"""name: ${keyword.name}, level=${keyword.level}, parent: ${if keyword.parent.nonEmpty then keyword.parent.get.name else ""}""")

    val keywords = KeywordAnalyzer.buildKeywordList("C:/work/Lightroom Keywords.txt")
    val duplicates = KeywordAnalyzer.findDuplicates(keywords, false)
    println(s"Number of duplicates = ${duplicates.size}")
    duplicates.foreach(duplicate =>
      val name = duplicate._1
      val list = duplicate._2
      println(s"duplicated name: $name")
      list.foreach(output)
    )
  }
end KeywordAnalyzerTest

