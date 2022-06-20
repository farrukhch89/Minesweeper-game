package farrukh.example.minesweeper

import java.util.*


object Cell {

    data class Point(var x : Short, var y : Short)
    val MINE: Short = -1
    var gameOver = false

    //cover states
    val COVERED: Short = -2
    val UNCOVERED: Short = -3
    val FLAGGED: Short = -4

    //modes
    val TRY: Short = -5
    val FLAG: Short = -6

    var numMines = 20
    val defaultNumFlags = numMines


    var mode = TRY
    var numFlags = defaultNumFlags

    var numRows = 10
    var numColumns = 10
    var Win = false

    private var model = generateRandomSetup()

    //change the cover state at the field (x, y)
    fun setCoverState(x: Int, y: Int, state: Short) {
        model[x][y].y = state

        numFlags = when {
            //when the action is flagging, decrease the number of available flags
            (state == FLAGGED) -> numFlags - 1
            //when the action is un-flagging, increase the number of available flags
            (state == COVERED) -> numFlags + 1
            else -> numFlags
        }

        checkGameOver()
        checkForWin()
        if (gameOver) {
            uncoverAllMines()
        }


    }

    //change the state of the field at (tX, tY) depending on whether in TRY or FLAG mode
    fun changeSquareState(tX: Int, tY: Int) {
        when (mode) {
            TRY -> {
                //if in TRY mode, uncover the square
                setCoverState(tX, tY, UNCOVERED)
                //if the square is empty (i.e. no mines nearby), uncover all squares around it
                if ((model[tX][tY].x).toInt() == 0) uncoverAllSquaresAround(tX, tY)
            }
            FLAG -> {
                //when in FLAG mode, toggle between FLAGGED and COVERED depending on
                // the current cover state of the square
                when (getCoverState(tX, tY)) {
                    FLAGGED -> setCoverState(tX, tY, COVERED)
                    COVERED -> {
                        //if player is not already out of flags, then they can flag the square
                        if (numFlags != 0) setCoverState(tX, tY, FLAGGED)
                    }
                }
            }
        }
    }



    fun getCoverState(x: Int, y: Int): Short = model[x][y].y

    //randomly place the mines on the board, then update the number of mines near each field
    fun generateRandomSetup(): Array<Array<Point>> {
        var array = Array<Array<Point>>(numColumns) { Array<Point>(numRows) {Point(0,COVERED)}}

        placeMines(array)

        //calculate numbers for each field based on its proximity to the mines
        for (i in 0 until numColumns) {
            for (j in 0 until numRows) {
                if (array[i][j].x != MINE) {
                    //set the mine counter for the current square
                    array[i][j].x = countMinesSquare(array, i, j).toShort()
                }
            }
        }
        return array
    }
    //place mines randomly on the board by generating pairs of random numbers
    private fun placeMines(array: Array<Array<Point>>) {

        val random = Random()
        for (i in 1..numMines) {
            var mineLocation = Pair(random.nextInt(numColumns), random.nextInt(numRows))
            //check that there isn't already a mine at this spot
            while (array[mineLocation.first][mineLocation.second].x == MINE) {
                mineLocation = Pair(random.nextInt(numColumns), random.nextInt(numRows))
            }
            array[mineLocation.first][mineLocation.second].x = MINE

        }
    }
    fun isMine(x: Int, y: Int): Boolean {
        var truth = false
        if (model[x][y].x == MINE) {
            truth = true
        }
        return truth
    }

    //if any mine is uncovered, then the game is over
    fun checkGameOver() {
        for (row in model) {
            for (square in row) {
                if (square.x == MINE && square.y == UNCOVERED) {
                    gameOver = true
                }
            }
        }
    }
    //uncover all the mines on the board
    private fun uncoverAllMines() {
        for (i in 0 until numColumns) {
            for (j in 0 until numRows) {
                if (model[i][j].x == MINE) {
                    model[i][j].y = UNCOVERED
                }
            }
        }
    }
    //returns the number of mines near a given square
    fun getMineCounter(x: Int, y: Int): Short = model[x][y].x

    fun countMinesSquare(array: Array<Array<Point>>, i: Int, j: Int): Int {
        var counter = 0
        if (i-1>=0 && j-1>=0 && array[i-1][j-1].x == MINE) counter +=1
        if (i-1>=0 && array[i-1][j].x == MINE) counter += 1
        if (i-1>=0 && j+1<numRows && array[i-1][j+1].x == MINE) counter +=1
        if (j-1>=0 && array[i][j-1].x == MINE) counter += 1
        if (j+1<numRows && array[i][j+1].x == MINE) counter += 1
        if (i+1<numColumns && j-1>=0 && array[i+1][j-1].x == MINE) counter += 1
        if (i+1<numColumns && array[i+1][j].x == MINE) counter += 1
        if (i+1<numColumns && j+1<numRows && array[i+1][j+1].x == MINE) counter +=1
        return counter
    }



    fun resetModel() {
        //restore all default values
        gameOver = false
        mode = TRY
        numFlags = defaultNumFlags
        Win = false

        //generate a new random setup
        model = generateRandomSetup()
    }

    private fun uncoverAllSquaresAround(tX: Int, tY: Int) {
        if (tX-1>=0 && tY-1>=0 && (model[tX-1][tY-1].y) != UNCOVERED) changeSquareState(tX - 1, tY - 1)
        if (tY-1>=0 && (model[tX][tY-1].y) != UNCOVERED) changeSquareState(tX, tY - 1)
        if (tX+1<numColumns && tY-1>=0 && (model[tX+1][tY-1].y) != UNCOVERED) changeSquareState(tX + 1, tY - 1)
        if (tX-1>=0 && (model[tX-1][tY].y) != UNCOVERED) changeSquareState(tX - 1, tY)
        if (tX+1<numColumns && (model[tX+1][tY].y) != UNCOVERED) changeSquareState(tX + 1, tY)
        if (tX-1>=0 && tY+1<numRows && (model[tX-1][tY+1].y) != UNCOVERED) changeSquareState(tX - 1, tY + 1)
        if (tY+1<numRows && (model[tX][tY+1].y) != UNCOVERED) changeSquareState(tX, tY + 1)
        if (tX+1<numColumns && tY+1<numRows && (model[tX+1][tY+1].y) != UNCOVERED) changeSquareState(tX + 1, tY + 1)
    }

    //check if player has won by making sure all mines are still covered (or flagged) and all non-mine squares are uncovered
    fun checkForWin() {
        var mineCounter = 0
        var nonMineCounter = 0
        for (row in model) {
            for (square in row) {
                if ((square.x == MINE && square.y == COVERED) || (square.x == MINE && square.y == FLAGGED)) {
                    mineCounter +=1
                }
                if (square.x != MINE && square.y == UNCOVERED) {
                    nonMineCounter +=1
                }
            }
        }
        //if all non-mine squares are uncovered and all mines are either covered or flagged, player wins
        if (mineCounter == numMines && nonMineCounter == ((numRows * numColumns) - numMines)) {
            Win = true
        }
    }



}

