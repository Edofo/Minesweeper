package runtime

import minesweeper.gui.MinesweeperGUI
import minesweeper.model.Board

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JButton}

object Game {

  val ROWS = 10
  val COLUMNS = 10
  val MINES = 10

  val MinesweeperGUI = new MinesweeperGUI(ROWS, COLUMNS)

  val board = new Board(ROWS, COLUMNS, MINES)

//   def main(args: Array[String]): Unit = {
//     MinesweeperGUI.show()
//   }

  def newGame(): Unit = {
    board.newGame()

    val boardArray = board.getBoard()

    for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
      MinesweeperGUI.setButtonEnabled(i, j, true)
      MinesweeperGUI.setButtonText(i, j, "")
    }
  }

  def exit(): Unit = {
    System.exit(0)
  }

  def main(args: Array[String]): Unit = {
    MinesweeperGUI.show()
    MinesweeperGUI.addNewGameListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        newGame()
      }
    })
    MinesweeperGUI.addExitListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        exit()
      }
    })
    MinesweeperGUI.addBoardListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        val button = e.getSource().asInstanceOf[JButton]
        val row = button.getClientProperty("row").asInstanceOf[Int]
        val col = button.getClientProperty("col").asInstanceOf[Int]
        val boardArray = board.getBoard()
        if (boardArray(row)(col) == 'X') {

          MinesweeperGUI.showMessage("You lost! :(")

          for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
            MinesweeperGUI.setButtonEnabled(i, j, false)
            MinesweeperGUI.setButtonText(i, j, boardArray(i)(j).toString)
          }
        } else {
          MinesweeperGUI.setButtonEnabled(row, col, false)
          MinesweeperGUI.setButtonText(row, col, boardArray(row)(col).toString)
        }
      }
    })
    newGame()
  }
}
