/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.ext
import com.stulsoft.file.tools.data.DataProvider

import java.io.File
import scala.swing.FileChooser.Result.Approve
import scala.swing.Swing.EtchedBorder
import scala.swing.event.{ButtonClicked, ValueChanged}
import scala.swing.*

class ListAllExtensionsWindow extends Frame {
  private val runButton: Button = new Button("Start search") {
    enabled = false
    reactions += {
      case ButtonClicked(_) =>
        resultList.text = "Please wait..."
        resultList.text = ListAllExtensions.findAllExtensions(path.text) match
          case Left(error) => "Error: " + error
          case Right(list) => list
    }
  }

  private val path = new TextField("Select path", 30) {
    reactions += {
      case ValueChanged(_) =>
        runButton.enabled = text.nonEmpty
    }
  }

  private val chooseFileButton = new Button("...") {
    reactions += {
      case ButtonClicked(_) =>
        val chooser = new FileChooser(new File(DataProvider.lastFile()))
        chooser.title = "Select directory"
        chooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
        val choice = chooser.showOpenDialog(this)
        if choice == Approve then
          val file = chooser.selectedFile
          DataProvider.storeLastFile(file.getAbsolutePath)
          path.text = file.getAbsolutePath
    }
  }

  private val resultList = new TextArea(10, 20) {
    editable = false
  }

  private val result = new ScrollPane {
    border = Swing.TitledBorder(EtchedBorder, "Result")
    contents = resultList
  }

  private val selectPanel = new FlowPanel(FlowPanel.Alignment.Left)(path, chooseFileButton, runButton) {
  }
  
  contents = new BorderPanel {
    layout(selectPanel) = BorderPanel.Position.North
    layout(result) = BorderPanel.Position.Center

  }

  title = "List all extensions"
  size = new Dimension(600, 300)
  centerOnScreen()

}
