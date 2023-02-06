/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.data

import org.apache.commons.io.output.WriterOutputStream

import java.io.{File, FileReader, FileWriter}
import java.util.Properties
import scala.io.Source

object DataProvider {
  enum Property:
    case LASTFILE

  private val properties: Properties = new Properties()

  private def buildFile(): File = {
    try {
      val dataFilePath = System.getenv("APPDATA")
      val dir = new File(dataFilePath + "/ys-file-tools")
      if !dir.exists() then dir.mkdir()
      val file = new File(dir.getAbsolutePath + "/ys-file-tools.properties")
      if !file.exists() then file.createNewFile()
      file
    } catch
      case exception: Exception =>
        exception.printStackTrace()
        throw exception
  }

  private def loadProperties(): Unit = {
    properties.load(new FileReader(buildFile()))
  }

  private def saveProperties(): Unit = {
    try {
      val writer = new FileWriter(buildFile())
      properties.store(writer, "The properties for ys-file-tools application")
      writer.close()
    }
    catch
      case exception: Exception => exception.printStackTrace()
  }

  private def saveProperty(key:String, value:String): Unit = {
    properties.setProperty(key, value)
    saveProperties()
  }

  def lastFile():String={
    properties.getProperty(Property.LASTFILE.toString)
  }

  def storeLastFile(filename:String):Unit={
    saveProperty(Property.LASTFILE.toString, filename)
  }

  loadProperties()

  def main(args: Array[String]): Unit = {
    DataProvider.properties.entrySet().forEach(p => println(s"${p.getKey} -> ${p.getValue}"))
    println("====")
    println(DataProvider.properties)
/*
    println(s"lastfile: ${lastFile()}")
    val fff = new File(lastFile())
    println(fff.isDirectory)
    println(properties)
*/
  }
}
