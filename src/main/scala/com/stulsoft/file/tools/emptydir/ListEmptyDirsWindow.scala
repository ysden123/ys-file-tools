/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.file.tools.emptydir

import java.io.File
import scala.swing.FileChooser.Result.Approve
import scala.swing.Swing.EtchedBorder
import scala.swing.event.{ButtonClicked, ValueChanged}
import scala.swing.{BorderPanel, Button, Dialog, Dimension, FileChooser, FlowPanel, Swing, TextArea, TextField, Window}

class ListEmptyDirsWindow(owner: Window) extends Dialog(owner) {
  private var initDir: File = _
  private val runButton: Button = new Button("Start search") {
    enabled = false
    reactions += {
      case ButtonClicked(_) =>
        result.text = ListEmptyDirs.buildListOfEmptyDirs(path.text)
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
        result.text = "Please wait..."
        val chooser = new FileChooser(initDir)
        chooser.title = "Select directory"
        chooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
        val choice = chooser.showOpenDialog(this)
        if choice == Approve then
          val file = chooser.selectedFile
          initDir = file
          path.text = file.getAbsolutePath
    }
  }
  private val result = new TextArea(10, 20) {
    border = Swing.TitledBorder(EtchedBorder, "Result")
    editable = false
  }

  private val selectPanel = new FlowPanel(FlowPanel.Alignment.Left)(path, chooseFileButton, runButton) {
  }

  contents = new BorderPanel{
    layout(selectPanel) = BorderPanel.Position.North
    layout(result) = BorderPanel.Position.Center

  }

  title = "List empty directories"
  size = new Dimension(600, 300)
  centerOnScreen()
}
