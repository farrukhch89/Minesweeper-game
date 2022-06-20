package farrukh.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import farrukh.example.minesweeper.Cell.FLAG
import farrukh.example.minesweeper.Cell.TRY

class MainActivity : AppCompatActivity() {
    private lateinit var btn_reset: Button
    private lateinit var num_mine:TextView
    private lateinit var marking_mode: Button
    var flagm = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_reset = findViewById<Button>(R.id.btn_reset)
        btn_reset.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Cell.resetModel()
            } })

        num_mine = findViewById<TextView>(R.id.num_mine)
        var count = Cell.numMines
        num_mine.setText("Number of Mines:  " + count.toString())

        // uncover and flag button
        val toggle: ToggleButton = findViewById(R.id.marking_mode)
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                Cell.mode = FLAG

            }
            else{
                Cell.mode = TRY
            }
        }
    }
}