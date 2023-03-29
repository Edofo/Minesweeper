package minesweeper.model

import scala.util.Random

class Board(rows: Int, columns: Int, mines: Int) {

  val ROWS = rows
  val COLUMNS = columns
  val MINES = mines

  // create a 2D array of characters
  val board = Array.ofDim[Char](ROWS, COLUMNS)

  val random = new Random()

  def initBoard(): Unit = {
    for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
      board(i)(j) = '_'
    }
  }

  def setMinePositions(): Unit = {
    for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
      board(i)(j) = '_'
    }

    for (i <- 0 until MINES) {
      val row = random.nextInt(ROWS)
      val col = random.nextInt(COLUMNS)
      board(row)(col) = 'X'
    }
  }

  def fillBoard(): Unit = {
    for (i <- 0 until ROWS; j <- 0 until COLUMNS) {
      if (board(i)(j) != 'X') {
        board(i)(j) = countMines(i, j).toString.charAt(0)
      }
    }
  }

  def isMine(row: Int, col: Int): Boolean = {
    if (row < 0 || row >= ROWS || col < 0 || col >= COLUMNS) {
      false
    } else {
      board(row)(col) == 'X'
    }
  }

  def getBoard(): Array[Array[Char]] = {
    board
  }

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

  def newGame(): Unit = {
    setMinePositions()
    fillBoard()
  }

}
