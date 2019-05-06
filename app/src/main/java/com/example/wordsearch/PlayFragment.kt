package com.example.wordsearch

import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_play.*

class PlayFragment : Fragment() {

    private val alphabet: List<Char> = mutableListOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    )
    private val columnSize = 10
    private val rowSize = 10
    private var wordLayoutWidth = 0
    private var wordLayoutHeight = 0
    private lateinit var wordGrid: Array<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wordGrid = Array(rowSize) { Array(columnSize) { "" } }
        findLocations()
        initLayouts()
    }

    private fun initLayouts() {
        val viewTreeObserver: ViewTreeObserver = parentLayout.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        wordLayoutHeight = wordLayout.height
                        wordLayoutWidth = wordLayout.width
                        setupTable()
                    }
                }
            )
        }
    }

    private fun setupTable() {
        val width = wordLayoutWidth / this.rowSize
        val height = wordLayoutHeight / this.columnSize
        for (i in 0 until this.rowSize) {
            val row = TableRow(context)
            row.layoutParams
            row.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            for (j in 0 until this.columnSize) {
                val tv = TextView(context)
                tv.apply {
                    gravity = Gravity.CENTER
                    layoutParams = TableRow.LayoutParams(width, height, 1.0F)
                    text = wordGrid[i][j]
                }
                row.addView(tv)
            }
            wordLayout?.addView(row)
        }
    }

    private fun findLocations() {
        val words = arrayOf("SWIFT", "KOTLIN", "OBJECTIVEC", "VARIABLE", "JAVA", "MOBILE")
        for (word in words) {
            val randomBackwards = (0..1).random()
//            val randomBackwards = 0
            // val randomDirection = (0..2).random()
            val randomDirection = 0
            val isBackwards = randomBackwards == 0
            var direction = ""
            if (randomDirection == 0) {
                // vertically
                placeVertically(isBackwards, word)
            } else if (randomDirection == 1) {
                // horizontal placement
                direction = "horizontal"
            } else {
                direction = "diagonal"
            }
        }
    }

    private fun placeVertically(backwards: Boolean, word: String) {
        var wordToBePlaced = word
        val availableStarts = rowSize - wordToBePlaced.length + 1
        val columnIndexes = arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        val initIndexes = arrayListOf<Int>()
        if (backwards) {
            wordToBePlaced = word.reversed()
            for(i in columnSize-1 downTo (columnSize - availableStarts))
                initIndexes.add(i)
        } else {
            for (i in 0 until availableStarts) initIndexes.add(i)
        }
        // first only potentially within the length of the board
        /*
            randomize the column you are putting it in
            randomize the row you are putting it in
            check if there is nothing there,
            or if the letter currently there is in the right place of the word
            works? you are done (finish loop)
            doesn't work?
            remove the random entry row from the list
            and pick another one at random
            end of loop
         */
        var valid = false
        while (!valid) {
            valid = false
            var foundPlacement = false
            val randomColumn = (0 until columnIndexes.size).random()
            val randomColumnIndex = columnIndexes[randomColumn] // index of the random column
            // keep looping through until we find a correct orientation for the specific column
            while (!foundPlacement) {
                if (initIndexes.size == 0) break
                foundPlacement = false
                val randomStartingRow = (0 until initIndexes.size).random()
                var initIndex = initIndexes[randomStartingRow]
                for (i in 0 until word.length) {
                    val letter = word.substring(i, i + 1)
                    val letterOnGrid = wordGrid[initIndex][randomColumnIndex]
                    // encountered placement where letter on the grid doesn't line up with word
                    if (letterOnGrid != letter && letterOnGrid != "") {
                        break
                    }
                    // no problems add to the grid
                    wordGrid[initIndex][randomColumnIndex] = letter
                    if(backwards) {
                        initIndex--
                    } else {
                        initIndex++
                    }
                    // if at the end of the for loop end loop
                    if (i == word.length - 1) {
                        foundPlacement = true
                        valid = true
                    }
                }
                if (!foundPlacement) {
                    initIndexes.removeAt(randomStartingRow)
                    if (initIndexes.size == 0) {
                        foundPlacement = true
                        if(backwards) {
                            for(i in columnSize-1 downTo (columnSize - availableStarts))
                                initIndexes.add(i)
                        } else {
                            for (i in 0 until availableStarts) initIndexes.add(i)
                        }
                    }
                }
            }
            columnIndexes.removeAt(randomColumn)
        }
}

companion object {
    fun newInstance() =
        PlayFragment().apply {

        }
}
}
