// create a minesweeper game with 10 rows and 10 columns
import scala.util.Random
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JButton, JFrame, JPanel}
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import java.awt.BorderLayout

object Main extends App {
  // create a minesweeper game with 10 rows and 10 columns
  val ROWS = 10
  val COLUMNS = 10
  val MINES = 10

  // create a 2D array of buttons
  val board = Array.ofDim[JButton](ROWS, COLUMNS)

  // create a 2D array of characters
  val board2 = Array.ofDim[Char](ROWS, COLUMNS)

  // create a frame
  val frame = new JFrame("Minesweeper")

  // create a panel
  val panel = new JPanel()

  val random = new Random()

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

  // set mine positions
  def setMinePositions(): Unit = {
    for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
      board2(i)(j) = '_'
    }

    for (i <- 0 until MINES) {
      val row = random.nextInt(ROWS)
      val col = random.nextInt(COLUMNS)
      board2(row)(col) = 'X'
    }
  }

  // fill the board with the number of mines in the 8 surrounding positions
  def fillBoard(): Unit = {
    for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
      if (board2(i)(j) != 'X') {
        board2(i)(j) = countMines(i, j).toString.charAt(0)
      }
    }
  }

  // check if the given position is a mine
  def isMine(row: Int, col: Int): Boolean = {
    if (row < 0 || row >= ROWS || col < 0 || col >= COLUMNS) {
      false
    } else {
      board2(row)(col) == 'X'
    }
  }

  // count the number of mines in the 8 surrounding positions
  def countMines(row: Int, col: Int): Int = {
    var count = 0
    if (isMine(row - 1, col - 1)) count += 1
    if (isMine(row - 1, col)) count += 1
    if (isMine(row - 1, col + 1)) count += 1
    if (isMine(row, col - 1)) count += 1
    if (isMine(row, col + 1)) count += 1
    if (isMine(row + 1, col - 1)) count += 1
    if (isMine(row + 1, col)) count += 1
    if (isMine(row + 1, col + 1)) count += 1
    count
  }

  def revealCasesAround(row: Int, col: Int): Unit = {
    // reveal the cases around the current case if case is 0 recursively call
    for (i <- -1 to 1; j <- -1 to 1) {
      if (row + i >= 0 && row + i < ROWS && col + j >= 0 && col + j < COLUMNS) {
        if (
          board2(row + i)(col + j) != 'X' && board(row + i)(col + j).isEnabled
        ) {
          board(row + i)(col + j)
            .setText(board2(row + i)(col + j).toString)
          board(row + i)(col + j).setEnabled(false)
          if (board2(row + i)(col + j) == '0') {
            revealCasesAround(row + i, col + j)
          }
        }
      }
    }
  }

  // display the game over message
  def gameOver(): Unit = {
    // show a message dialog
    JOptionPane.showMessageDialog(frame, "Game Over")
    // disable all the buttons
    for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
      board(i)(j).setEnabled(false)
    }
  }

  // display the game won message
  def gameWon(): Unit = {
    // show a message dialog
    JOptionPane.showMessageDialog(frame, "You Won")
    // disable all the buttons
    for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
      board(i)(j).setEnabled(false)
    }
  }

  // add an action listener to the buttons
  for (row <- 0 until ROWS; col <- 0 until COLUMNS) {
    board(row)(col).addActionListener(new ActionListener() {
      def actionPerformed(e: ActionEvent): Unit = {

        // board(row)(col).setText(board2(row)(col).toString)
        // board(row)(col).setEnabled(false)

        // check if the game is over
        if (board2(row)(col) == 'X') {
          // show a message dialog
          gameOver()
          // disable all the buttons
          for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
            board(i)(j).setEnabled(false)
          }
        } else {

          if (board2(row)(col) == '0') {
            revealCasesAround(row, col)
          } else {
            board(row)(col).setText(board2(row)(col).toString)
            board(row)(col).setEnabled(false)
          }

          // check if the game is won
          var count = 0

          for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
            if (!board(row)(col).isEnabled) {
              count += 1
            }
          }

          if (count == ROWS * COLUMNS - MINES) {
            gameWon()
          }
        }
      }
    })
  }

  // add an action listener to the buttonNew
  buttonNew.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent): Unit = {
      // create a new game
      newGame()
    }
  })

  // add an action listener to the buttonExit
  buttonExit.addActionListener(new ActionListener() {
    def actionPerformed(e: ActionEvent): Unit = {
      // exit the game
      System.exit(0)
    }
  })

  // create a new game
  def newGame(): Unit = {
    // set mine positions
    setMinePositions()
    // fill the board with the number of mines in the 8 surrounding positions
    fillBoard()
    // enable all the buttons
    for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
      board(i)(j).setEnabled(true)
      board(i)(j).setText("")
    }

    // show the frame
    frame.setVisible(true)
  }

  // create a new game
  newGame()

}
