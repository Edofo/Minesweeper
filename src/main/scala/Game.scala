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

  def revealCasesAround(row: Int, col: Int): Unit = {
    // reveal the cases around the current case if case is 0 recursively call
    for (i <- -1 to 1; j <- -1 to 1) {
      if (row + i >= 0 && row + i < ROWS && col + j >= 0 && col + j < COLUMNS) {
        if (
          board.getBoard()(row + i)(col + j) != 'X' && board.getBoard()(
            row + i
          )(col + j) != '0'
        ) {
          MinesweeperGUI.setButtonText(
            row + i,
            col + j,
            board.getBoard()(row + i)(col + j).toString
          )
          MinesweeperGUI.setButtonEnabled(row + i, col + j, false)
        } else if (board.getBoard()(row + i)(col + j) == '0') {
          MinesweeperGUI.setButtonText(
            row + i,
            col + j,
            board.getBoard()(row + i)(col + j).toString
          )
          MinesweeperGUI.setButtonEnabled(row + i, col + j, false)
          revealCasesAround(row + i, col + j)
        }
      }
    }
  }

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

          if (boardArray(row)(col) == '0') {
            revealCasesAround(row, col)
          } else {
            MinesweeperGUI.setButtonText(
              row,
              col,
              boardArray(row)(col).toString
            )
            MinesweeperGUI.setButtonEnabled(row, col, false)
          }

          // Check if the player won
          var won = true

          for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
            if (
              boardArray(i)(j) != 'X' && MinesweeperGUI.getButtonEnabled(
                i,
                j
              )
            ) {
              won = false
            }
          }

          if (won) {
            MinesweeperGUI.showMessage("You won! :)")
          }
        }
      }
    })

    newGame()
  }
}
