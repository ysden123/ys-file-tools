/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.ext

import com.stulsoft.file.tools.data.DataProvider

import java.io.File
import javax.swing.SwingUtilities
import scala.swing.*
import scala.swing.FileChooser.Result.Approve
import scala.swing.Swing.EtchedBorder
import scala.swing.event.{ButtonClicked, ValueChanged}
import scala.util.{Failure, Success}

given ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

class ListAllExtensionsFrame extends BorderPanel {
  private val runButton: Button = new Button("Start search") {
    enabled = false
    reactions += {
      case ButtonClicked(_) =>
        resultList.text = "Please wait..."
        SwingUtilities.invokeLater(() => {
          ListAllExtensions.findAllExtensions(path.text).onComplete {
            case Success(result) =>
              result match
                case Left(error) => resultList.text = "Error: " + error
                case Right(list) => resultList.text = list

            case Failure(exception) => resultList.text = "Error: " + exception.getMessage
          }
        })
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

  layout(selectPanel) = BorderPanel.Position.North
  layout(result) = BorderPanel.Position.Center
  border = Swing.TitledBorder(EtchedBorder, "List all extensions")
}