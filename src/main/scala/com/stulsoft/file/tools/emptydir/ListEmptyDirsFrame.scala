/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.emptydir

import com.stulsoft.file.tools.data.DataProvider

import java.io.File
import javax.swing.SwingUtilities
import scala.swing.*
import scala.swing.FileChooser.Result.Approve
import scala.swing.Swing.EtchedBorder
import scala.swing.event.{ButtonClicked, ValueChanged}
import scala.util.{Failure, Success}

given ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

class ListEmptyDirsFrame extends BorderPanel {
  private val runButton: Button = new Button("Start search") {
    enabled = false
    reactions += {
      case ButtonClicked(_) =>
        result.text = "Please wait..."
        SwingUtilities.invokeLater(() => {
          ListEmptyDirs.buildListOfEmptyDirs(path.text).onComplete {
            case Success(emptyDirs) => result.text = emptyDirs
            case Failure(exception) => result.text = exception.getMessage
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
  private val result = new TextArea(10, 20) {
    border = Swing.TitledBorder(EtchedBorder, "Result")
    editable = false
  }

  private val selectPanel = new FlowPanel(FlowPanel.Alignment.Left)(path, chooseFileButton, runButton) {
  }

  layout(selectPanel) = BorderPanel.Position.North
  layout(result) = BorderPanel.Position.Center

  border = Swing.TitledBorder(EtchedBorder, "List empty directories")
}
