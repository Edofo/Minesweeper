import scala.util.Random
import java.awt.event.ActionEvent
import javax.swing._
import java.awt.BorderLayout

// create a minesweeper game with 10 rows and 10 columns
object Minesweeper extends App {

  // Set the number of rows, columns, and mines
  val ROWS = 10
  val COLUMNS = 10
  val MINES = 10

  // Create the frame and the board and control panel
  val frame = new JFrame("Minesweeper")
  val board = new Board(ROWS, COLUMNS)
  val controlPanel = new ControlPanel(board)

  // Add the control panel and the board to the frame
  frame.add(controlPanel, BorderLayout.NORTH)
  frame.add(board, BorderLayout.CENTER)
  // frame.setDefaultCloseOperation(WindowConstants.EXIT_ONCLOSE)
  frame.setSize(500, 500)
  frame.setVisible(true)

  // Create the control panel
  class ControlPanel(board: Board) extends JPanel {
    // Create the buttons
    val newGameButton = new JButton("New Game")
    val exitButton = new JButton("Exit")

    // Add the action listeners
    newGameButton.addActionListener((e: ActionEvent) => board.newGame())
    exitButton.addActionListener((e: ActionEvent) => System.exit(0))

    // Add the buttons to the panel
    setLayout(new java.awt.FlowLayout())
    add(newGameButton)
    add(exitButton)
  }

  // Create the board
  class Board(rows: Int, cols: Int) extends JPanel {
    // Create the buttons and the mines array
    private val random = new Random()
    private val buttons = Array.ofDim[JButton](rows, cols)
    private val mines = Array.ofDim[Char](rows, cols)

    // Add the buttons to the panel
    setLayout(new java.awt.GridLayout(rows, cols))

    // Add the buttons to the panel
    for (i <- 0 until rows; j <- 0 until cols) {
      val button = new JButton()
      // Handle a click on a button
      button.addActionListener((e: ActionEvent) => handleClick(i, j))
      buttons(i)(j) = button
      add(button)
    }

    // Create a new game
    def newGame(): Unit = {
      setMinePositions()
      fillBoard()
      for (i <- 0 until rows; j <- 0 until cols) {
        buttons(i)(j).setEnabled(true)
        buttons(i)(j).setText("")
      }
    }

    // Set the mine positions
    private def setMinePositions(): Unit = {
      for (i <- 0 until rows; j <- 0 until cols) mines(i)(j) = '_'
      for (i <- 0 until MINES) {
        val row = random.nextInt(rows)
        val col = random.nextInt(cols)
        mines(row)(col) = 'X'
      }
    }

    // Fill the board with the number of mines around each cell
    private def fillBoard(): Unit = {
      for (i <- 0 until rows; j <- 0 until cols) {
        if (mines(i)(j) != 'X')
          mines(i)(j) = countMines(i, j).toString.charAt(0)
      }
    }

    // Reveal all the board
    private def revealAllBoard(): Unit = {
      for (i <- 0 until rows; j <- 0 until cols) {
        buttons(i)(j).setEnabled(false)
        buttons(i)(j).setText(mines(i)(j).toString)
      }
    }

    // Check if a cell is a mine
    private def isMine(row: Int, col: Int): Boolean = {
      row >= 0 && row < rows && col >= 0 && col < cols && mines(row)(col) == 'X'
    }

    // Count the number of mines around a cell
    private def countMines(row: Int, col: Int): Int = {
      val directions = List(
        (-1, -1),
        (-1, 0),
        (-1, 1),
        (0, -1),
        (0, 1),
        (1, -1),
        (1, 0),
        (1, 1)
      )
      directions.count { case (dr, dc) => isMine(row + dr, col + dc) }
    }

    // Reveal the cases around a cell
    private def revealCasesAround(row: Int, col: Int): Unit = {
      for (i <- -1 to 1; j <- -1 to 1) {
        val newRow = row + i
        val newCol = col + j
        if (
          newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && mines(
            newRow
          )(newCol) != 'X' && buttons(newRow)(newCol).isEnabled
        ) {
          buttons(newRow)(newCol).setText(mines(newRow)(newCol).toString)
          buttons(newRow)(newCol).setEnabled(false)
          if (mines(newRow)(newCol) == '0') revealCasesAround(newRow, newCol)
        }
      }
    }

    // Display a message when the game is over
    private def gameOver(): Unit = {
      JOptionPane.showMessageDialog(frame, "Game Over")
      revealAllBoard()
    }

    // Display a message when the game is won
    private def gameWon(): Unit = {
      JOptionPane.showMessageDialog(frame, "You Won")
      revealAllBoard()
    }

    // Handle a click on a button
    private def handleClick(row: Int, col: Int): Unit = {
      // If the button is a mine, the game is over
      if (mines(row)(col) == 'X') gameOver()
      else {
        // If the button is not a mine, reveal it and the cases around it
        if (mines(row)(col) == '0') revealCasesAround(row, col)
        else {
          buttons(row)(col).setText(mines(row)(col).toString)
          buttons(row)(col).setEnabled(false)
        }
        // If all the buttons are revealed, the game is won
        if (buttons.flatten.count(!_.isEnabled) == rows * cols - MINES)
          gameWon()
      }

    }

  }

  // Create an implicit class to convert a function to an action listener
  implicit class ActionListenerExt(func: ActionEvent => Unit)
      extends java.awt.event.ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = func(e)
  }

  // Start a new game
  board.newGame()

}
