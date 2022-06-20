package farrukh.example.minesweeper

import android.R.attr
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import android.graphics.Point
import android.view.MotionEvent
import farrukh.example.minesweeper.Cell.COVERED
import farrukh.example.minesweeper.Cell.FLAGGED
import farrukh.example.minesweeper.Cell.UNCOVERED
import farrukh.example.minesweeper.Cell.gameOver
import kotlin.math.roundToInt


class MineCustomView(context: Context, attrs: AttributeSet) : View(context, attrs){


    // Paint object for coloring and styling
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    // Some colors for the face background, eyes and mouth.
    private var borders = Color.WHITE
    private var board = Color.BLACK
    private var borderColor = Color.WHITE
    private var width = 0f
    private var small_square = 0f
    private var touchedDownCell: Point? = null
    private val uncoverMode = true
    var paint_gray = Color.GRAY
    val paint_M = Paint(Paint.ANTI_ALIAS_FLAG)
    val paint_Black = Paint(Paint.ANTI_ALIAS_FLAG)
    val paint_box = Paint(Paint.ANTI_ALIAS_FLAG)
    var paintTextWhite = Paint(Paint.ANTI_ALIAS_FLAG)
    var paintTextRed = Color.RED
    var paintTextBlack = Color.BLACK
    var paintboxYellow = Color.YELLOW
    var paintTextwhite = Color.WHITE




    override fun onDraw(canvas: Canvas) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        drawSquare(canvas)
        drawlines(canvas)
        drawCell(canvas)

    }


    private fun drawSquare(canvas: Canvas){
        paint.color = board
        //1
        width = canvas.width.toFloat()

        //2
        val shapeBounds = canvas.drawRect(0f,0f, width, width, paint)


    }

    private fun drawlines(canvas: Canvas) {
        paint.color = borders
        width = canvas.width.toFloat()
        small_square = width / 10;


        for (i in 1 until 10) {
            canvas.drawLine(i * small_square, 0F, i * small_square, attr.height.toFloat(), paint)
        }

        for (i in 1 until 10) {
            canvas.drawLine(0F, i * small_square, width, i * small_square, paint)
        }

    }

    private fun drawCell(canvas: Canvas){
        for(i in 0 until 10){
            for(j in 0 until 10){
                drawCellSymbol(i,j,canvas)
            }
        }
    }

    private fun drawCellSymbol(row: Int, column: Int, canvas: Canvas) {
        var cellheight = small_square
        var cellwidth = small_square
        paint.color = paint_gray
        paint_M.color= paintTextRed
        paint_Black.color = paintTextBlack
        paint_box.color = paintboxYellow
        paintTextWhite.color = paintTextwhite
        //if FLAG
        if (Cell.getCoverState(row, column) == FLAGGED) {
            //add flag symbol
            canvas.drawRect(
                row * cellwidth,
                column * cellheight,
                (row + 1) * cellwidth,
                (column + 1) * cellheight,
                paint_box
            )
        }
        if(Cell.getCoverState(row,column) == UNCOVERED){
            canvas.drawRect(row * cellwidth, column * cellheight, (row+1)* cellwidth,(column+1) *cellheight, paint )

            if (Cell.isMine(row, column)) {
                //add mine symbol

                canvas.drawRect(
                    row * cellwidth,
                    column * cellheight,
                    (row + 1) * cellwidth,
                    (column + 1) * cellheight,
                    paint_M
                )
                paint_Black.setTextSize(40F);
                canvas.drawText(
                    "M",
                    row * cellwidth + (0.3 * cellwidth).toFloat(),
                    (column + 1) * cellheight - (0.3 * cellheight).toFloat(),
                    paint_Black
                )
            }
            else {
                paintTextWhite.setTextSize(40f)
                var counter = Cell.getMineCounter(row, column)
                if (counter != 0.toShort()) {
                    //add number of mines nearby
                    canvas.drawText(counter.toString(), row * cellwidth + (.4 * cellwidth).toFloat(), (column + 1) * cellheight - (.4 * cellheight).toFloat(), paintTextWhite)
                }
            }
        }


    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        val tX = event.x.toInt() / (width / Cell.numColumns)
        val tY = event.y.toInt() / (width / Cell.numRows)

        //if the game is not over, change the state of the field at (tX, tY) in the model
        if(gameOver == false ) {
            if ((tX <= Cell.numColumns) && (tY <= Cell.numRows)) {

                if (Cell.getCoverState(tX.toInt(), tY.toInt()) == COVERED)
                    Cell.changeSquareState(tX.toInt(), tY.toInt())

            }
        }

        invalidate()

        return super.onTouchEvent(event)
    }

    override fun onSizeChanged(width: Int, height: Int, oldwidth: Int, oldheight: Int) {
        super.onSizeChanged(width, height, oldwidth, oldheight)



    }




}

