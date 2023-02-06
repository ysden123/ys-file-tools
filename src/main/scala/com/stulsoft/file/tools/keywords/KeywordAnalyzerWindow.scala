/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.keywords

import com.stulsoft.file.tools.data.DataProvider

import java.io.File
import javax.swing.filechooser.FileFilter
import scala.swing.FileChooser.Result.Approve
import scala.swing.TabbedPane.Page
import scala.swing.event.{ButtonClicked, ValueChanged}
import scala.swing.*

class KeywordAnalyzerWindow extends Frame {
  private var keywords: Iterable[Keyword] = Nil

  private val runButton: Button = new Button("Analyze") {
    enabled = false
    reactions += {
      case ButtonClicked(_) =>
        keywords = KeywordAnalyzer.buildKeywordList(path.text)
        val statistics = KeywordAnalyzer.buildKeywordStatistics(keywords)
        statisticsList.text = ""
        statisticsList.text += s"Number of the root keywords: ${statistics.rootKeywordCount}\n"
        statisticsList.text += s"Max level: ${statistics.maxLevel}\n"

        duplicateList.text = ""
        val duplicates = KeywordAnalyzer.findDuplicates(keywords, true)
        duplicates.foreach(duplicate => {
          duplicateList.text += s"${duplicate._1}:\n"
          duplicate._2.foreach(keyword => {
            val parent = if keyword.parent.nonEmpty then keyword.parent.get.name else ""
            duplicateList.text += s"   on ${keyword.level} level with parent: $parent\n"
          })
        })
    }
  }

  private val path = new TextField("Select file", 30) {
    reactions += {
      case ValueChanged(_) =>
        runButton.enabled = text.nonEmpty
    }
  }

  private val chooseFileButton = new Button("...") {
    reactions += {
      case ButtonClicked(_) =>
        val chooser = new FileChooser(new File(DataProvider.lastFile()))
        chooser.title = "Select file to analyze keywords"
        chooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
        chooser.fileFilter = new FileFilter {
          override def accept(f: File): Boolean = {
            if f.isFile then
              val extension = f.getPath.toUpperCase()
              extension.endsWith(".TXT")
            else
              true
          }

          override def getDescription: String = "Exported file (.txt)"
        }
        val choice = chooser.showOpenDialog(this)
        if choice == Approve then
          val file = chooser.selectedFile
          DataProvider.storeLastFile(file.getAbsolutePath)
          path.text = file.getAbsolutePath
    }
  }

  private val statisticsList = new TextArea() {
    editable = false
  }

  private val statisticsPane = new ScrollPane {
    contents = statisticsList
  }

  private val duplicateList = new TextArea() {
    editable = false
  }

  private val duplicatePane = new ScrollPane {
    contents = duplicateList
  }

  private lazy val tabs = new TabbedPane {
    pages += new Page("Statistics", statisticsPane)
    pages += new Page("Duplicates", duplicatePane)
  }

  private val selectPanel = new FlowPanel(FlowPanel.Alignment.Left)(path, chooseFileButton, runButton) {
  }
  
  contents = new BorderPanel{
    layout(selectPanel) = BorderPanel.Position.North
    layout(tabs) = BorderPanel.Position.Center
  }
  
  title = "Keyword analysis"
  size = new Dimension(600, 300)
  centerOnScreen()
}
