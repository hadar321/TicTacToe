package com.example.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    private val players: Array<Player> = arrayOf(Player("X", "#be0af5"), Player("O", "#d91434"))
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mainLayout: ConstraintLayout = findViewById(R.id.main)
        val title: TextView = findViewById(R.id.title)
        val textViews: Array<Array<TextView>> = Array(3) { i ->
            Array(3) { j ->
                mainLayout.findViewWithTag((i * 3 + j + 1).toString())
            }
        }

        for (i in 0..2) {
            for (j in 0..2) {
                textViews[i][j].setOnClickListener { view ->
                    clicked(
                        view as TextView,
                        textViews,
                        title
                    )
                }
            }
        }
    }

    private fun clicked(tile: TextView, textViews: Array<Array<TextView>>, title: TextView) {
        val currPlayer: Player = players[counter % 2]
        tile.text = currPlayer.text
        tile.setTextColor(Color.parseColor(currPlayer.colorString))
        counter++
        if (isWinner(currPlayer.text, textViews)) {
            winAlert(String.format("%s is the WINNER!", currPlayer.text), textViews, title)
        } else if (counter == 9) {
            winAlert("It is a Tie!", textViews, title)
        } else {
            title.text = String.format("%s Play", players[counter % 2].text)
            tile.isClickable = false
        }
    }

    private fun isWinner(player: String, textViews: Array<Array<TextView>>): Boolean {
        var checkCounter = 0
        for (row in 0..2) {
            for (col in 0..2) {
                if (textViews[row][col].text === player) checkCounter++
            }
            if (checkCounter == 3) return true
            checkCounter = 0
        }
        for (col in 0..2) {
            for (row in 0..2) {
                if (textViews[row][col].text === player) checkCounter++
            }
            if (checkCounter == 3) return true
            checkCounter = 0
        }
        var col = 0
        for (row in 0..2) {
            if (textViews[row][col].text === player) checkCounter++
            col++
        }
        if (checkCounter == 3) return true
        col = 2
        checkCounter = 0
        for (row in 0..2) {
            if (textViews[row][col].text === player) checkCounter++
            col--
        }
        return checkCounter == 3
    }

    private fun winAlert(text: String, textViews: Array<Array<TextView>>, title: TextView) {
        for (row in 0..2) for (col in 0..2) textViews[row][col].isClickable = false
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setCancelable(false)
        dialog.setTitle(text)
        dialog.setPositiveButton(
            "New Game"
        ) { _, _ ->
            for (row in 0..2) for (col in 0..2) {
                textViews[row][col].isClickable = true
                textViews[row][col].text = ""
            }
            counter = 0
            title.text = String.format("%s Play", players[counter % 2].text)
        }
        dialog.show()
    }
}