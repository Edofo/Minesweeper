package minesweeper.gui

import java.awt.BorderLayout
import java.awt.event.{ActionListener}
import javax.swing.{JButton, JFrame, JPanel}
import javax.swing.JOptionPane

class MinesweeperGUI(rows: Int, columns: Int) {

  val ROWS = rows
  val COLUMNS = columns

  // create a 2D array of buttons
  val board = Array.ofDim[JButton](ROWS, COLUMNS)

  // create a frame
  val frame = new JFrame("Minesweeper")

  // create a panel
  val panel = new JPanel()

  // create a buttons
  val buttonNew = new JButton("New Game")
  val buttonExit = new JButton("Exit")

  // create a panel for the buttons
  val panelButtons = new JPanel()
  panelButtons.setLayout(new java.awt.FlowLayout())

  // add the buttons to the panel
  panelButtons.add(buttonNew)
  panelButtons.add(buttonExit)

  // add the panel to the frame
  frame.add(panelButtons, BorderLayout.NORTH)

  // add the panel to the frame
  frame.add(panel)

  // set the size of the frame
  frame.setSize(500, 500)

  // set the layout of the panel
  panel.setLayout(new java.awt.GridLayout(ROWS, COLUMNS))

  // add the buttons to the panel
  for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
    board(i)(j) = new JButton()
    panel.add(board(i)(j))
  }

  def show(): Unit = {
    frame.setVisible(true)
  }

  def addNewGameListener(listener: ActionListener): Unit = {
    buttonNew.addActionListener(listener)
  }

  def addExitListener(listener: ActionListener): Unit = {
    buttonExit.addActionListener(listener)
  }

  def addBoardListener(listener: ActionListener): Unit = {
    for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
      board(i)(j).addActionListener(listener)
    }
  }

  def getBoard(): Array[Array[JButton]] = {
    board
  }

  def showMessage(message: String): Unit = {
    JOptionPane.showMessageDialog(frame, message)
  }

  def close(): Unit = {
    frame.dispose()
  }

  def setButtonText(row: Int, column: Int, text: String): Unit = {
    board(row)(column).setText(text)
  }

  def setButtonEnabled(row: Int, column: Int, enabled: Boolean): Unit = {
    board(row)(column).setEnabled(enabled)
  }

}
