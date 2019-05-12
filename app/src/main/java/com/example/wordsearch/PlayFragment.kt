package com.example.wordsearch

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_play.*
import java.lang.Math.*
import kotlin.math.floor
import android.support.v4.view.ViewPager.OnPageChangeListener


class PlayFragment : Fragment() {

    private val alphabet: List<String> = mutableListOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
        "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    )
    private var words = arrayListOf("")
    private val wordMap = HashMap<String, IntArray>()
    private var columnSize = 0
    private var rowSize = 0
    private var wordLayoutWidth = 0
    private var wordLayoutHeight = 0
    private var loaded = false

    private val colors = arrayOf("#DC143C", "#FFA500", "#FFD700", "#483D8B", "#800080", "#006400", "#0000FF", "#800000")
    private var colorCount = 0
    private val highlightedMap = HashMap<String, String>()
    private var initHighlightedRow = -1
    private var initHighlightedColumn = -1
    private var lastHighlightedRow = -1
    private var lastHighlightedColumn = -1
    private var highlightedLetters = ""
    private var highlightedCounter = 0
    private var highlightedIndexes = ArrayList<IntArray>()

    private lateinit var wordGrid: Array<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val pager = (activity as MainActivity).getViewPager()
        pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if(position == 0) {
                    Log.d("Tag", "Inside")
                    activity?.let {
                        val gameData = ViewModelProviders.of(it).get(GameData::class.java)
                        observeInput(gameData)
                    }
                }
            }
        })
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeInput(gameData: GameData) {
        gameData.data.observe(this, Observer {
            it?.let {
                words = it["words"] as ArrayList<String>
                columnSize = it["columnSize"] as Int
                rowSize = it["rowSize"] as Int
                if(rowSize != 0 && columnSize != 0 && !words.isEmpty() && !loaded) {
                    Log.d("TAG", "Starting Game")
                    loaded = true
                    wordGrid = Array(rowSize) { Array(columnSize) { "" } }
                    findLocations()
                    initLayouts()
                }
            }
        })
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
                        setupBank()
                    }
                }
            )
        }
    }

    private fun setupBank() {
        val width = wordBankLayout.width
        val height = wordBankLayout.height
        val maxRows = 4
        val maxCells = 3
        var count = 0

        // max three table rows
        for (i in 0 until maxRows) {
            val row = TableRow(context)
            row.layoutParams
            row.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            for (j in 0 until maxCells) {
                Log.d("Tag", count.toString())
                if (count >= words.size)
                    break
                wordMap[words[count]] = intArrayOf(i, j)
                val tv = TextView(context)
                tv.apply {
                    gravity = Gravity.CENTER
                    layoutParams = TableRow.LayoutParams(width / maxCells, height / maxCells, 1.0F)
                    text = words[count]
                }
                row.addView(tv)
                count++
            }
            wordBankLayout?.addView(row)
            if (count == words.size)
                break
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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
        wordLayout.setOnTouchListener(View.OnTouchListener { v, event ->
            val view = v as TableLayout
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    val columnIndex = floor(event.y / height).toInt()
                    val rowIndex = floor(event.x / width).toInt()
                    if (initHighlightedRow == -1 && initHighlightedColumn == -1) {
                        initHighlightedRow = rowIndex
                        initHighlightedColumn = columnIndex
                        Log.d("stfu", "$initHighlightedRow $initHighlightedColumn")
                    }
                    if (columnIndex > -1 && rowIndex > -1 && columnIndex < columnSize && rowIndex < rowSize) {
                        val row = view.getChildAt(columnIndex) as TableRow
                        val cell = row.getChildAt(rowIndex) as TextView
                        val text = cell.text.toString()
//                        Log.d("Tag", "$text $rowIndex $columnIndex")
                        if (lastHighlightedColumn != columnIndex || lastHighlightedRow != rowIndex) {
                            lastHighlightedColumn = columnIndex
                            lastHighlightedRow = rowIndex
                            if (highlightedMap.containsKey(columnIndex.toString() + rowIndex.toString()) && highlightedLetters.isNotEmpty()) {
                                highlightedLetters = highlightedLetters.substring(0, highlightedLetters.length - 1)
                                highlightedMap.remove(columnIndex.toString() + rowIndex.toString())
                            } else {
                                highlightedCounter++
                                val differenceRow = abs(rowIndex - initHighlightedRow)
                                val differenceColumn = abs(columnIndex - initHighlightedColumn)
                                val bottomLeft = rowIndex < initHighlightedRow && columnIndex > initHighlightedColumn
                                val bottomRight = rowIndex > initHighlightedRow && columnIndex > initHighlightedColumn
                                val upLeft = rowIndex < initHighlightedRow && columnIndex < initHighlightedColumn
                                val upRight = rowIndex > initHighlightedRow && columnIndex < initHighlightedColumn
                                // when it is a diagonal moving
                                if (differenceRow == differenceColumn && (differenceRow != 0 && differenceColumn != 0)) {
                                    for (arr in highlightedIndexes) {
                                        val differenceY = arr[0] - columnIndex
                                        val differenceX = arr[1] - rowIndex
                                        if (differenceY != differenceX) {
                                            val currentRow = view.getChildAt(arr[0]) as TableRow
                                            val currentCell = currentRow.getChildAt(arr[1]) as TextView
                                            currentCell.setTextColor(Color.parseColor("#808080"))
                                            currentCell.typeface =
                                                Typeface.create(currentCell.typeface, Typeface.NORMAL)
                                        }
                                    }
                                    if (bottomLeft) {
                                        highlightedLetters = ""
                                        for (i in 0 until differenceRow + 1) {
                                            highlightedLetters += wordGrid[initHighlightedColumn + i][initHighlightedRow - i]
                                            val currentRow = view.getChildAt(initHighlightedColumn + i) as TableRow
                                            val currentCell = currentRow.getChildAt(initHighlightedRow - i) as TextView
                                            if (!currentCell.typeface.isBold) {
                                                currentCell.setTextColor(Color.parseColor(colors[colorCount]))
                                                currentCell.setTypeface(null, Typeface.BOLD)
                                                highlightedIndexes.add(
                                                    intArrayOf(
                                                        initHighlightedColumn + i,
                                                        initHighlightedRow - i
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    if (bottomRight) {
                                        highlightedLetters = ""
                                        for (i in 0 until differenceRow + 1) {
                                            highlightedLetters += wordGrid[initHighlightedColumn + i][initHighlightedRow + i]
                                            val currentRow = view.getChildAt(initHighlightedColumn + i) as TableRow
                                            val currentCell = currentRow.getChildAt(initHighlightedRow + i) as TextView
                                            if (!currentCell.typeface.isBold) {
                                                currentCell.setTextColor(Color.parseColor(colors[colorCount]))
                                                currentCell.setTypeface(null, Typeface.BOLD)
                                                highlightedIndexes.add(
                                                    intArrayOf(
                                                        initHighlightedColumn + i,
                                                        initHighlightedRow + i
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    if (upLeft) {
                                        highlightedLetters = ""
                                        for (i in 0 until differenceRow + 1) {
                                            highlightedLetters += wordGrid[initHighlightedColumn - i][initHighlightedRow - i]
                                            val currentRow = view.getChildAt(initHighlightedColumn - i) as TableRow
                                            val currentCell = currentRow.getChildAt(initHighlightedRow - i) as TextView
                                            if (!currentCell.typeface.isBold) {
                                                currentCell.setTextColor(Color.parseColor(colors[colorCount]))
                                                currentCell.setTypeface(null, Typeface.BOLD)
                                                highlightedIndexes.add(
                                                    intArrayOf(
                                                        initHighlightedColumn - i,
                                                        initHighlightedRow - i
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    if (upRight) {
                                        highlightedLetters = ""
                                        for (i in 0 until differenceRow + 1) {
                                            highlightedLetters += wordGrid[initHighlightedColumn - i][initHighlightedRow + i]
                                            val currentRow = view.getChildAt(initHighlightedColumn - i) as TableRow
                                            val currentCell = currentRow.getChildAt(initHighlightedRow + i) as TextView
                                            if (!currentCell.typeface.isBold) {
                                                currentCell.setTextColor(Color.parseColor(colors[colorCount]))
                                                currentCell.setTypeface(null, Typeface.BOLD)
                                                highlightedIndexes.add(
                                                    intArrayOf(
                                                        initHighlightedColumn - i,
                                                        initHighlightedRow + i
                                                    )
                                                )
                                            }
                                        }
                                    }
                                } else if (differenceColumn == 0 && differenceRow != 0) {
                                    Log.d("Tag", "Across")
                                    for (arr in highlightedIndexes) {
                                        val differenceY = arr[0] - columnIndex
                                        val differenceX = arr[1] - rowIndex
                                        if (differenceY != 0 || differenceX == 0) {
                                            val currentRow = view.getChildAt(arr[0]) as TableRow
                                            val currentCell = currentRow.getChildAt(arr[1]) as TextView
                                            currentCell.setTextColor(Color.parseColor("#808080"))
                                            currentCell.typeface =
                                                Typeface.create(currentCell.typeface, Typeface.NORMAL)
                                        }
                                    }
                                    if (initHighlightedRow > rowIndex) {
                                        Log.d("Tag", "Backwards")
                                        highlightedLetters = ""
                                        for (i in 0 until differenceRow + 1) {
                                            highlightedLetters += wordGrid[initHighlightedColumn][initHighlightedRow - i]
                                            val currentRow = view.getChildAt(initHighlightedColumn) as TableRow
                                            val currentCell = currentRow.getChildAt(initHighlightedRow - i) as TextView
                                            if (!currentCell.typeface.isBold) {
                                                currentCell.setTextColor(Color.parseColor(colors[colorCount]))
                                                currentCell.setTypeface(null, Typeface.BOLD)
                                                highlightedIndexes.add(
                                                    intArrayOf(
                                                        initHighlightedColumn,
                                                        initHighlightedRow - i
                                                    )
                                                )
                                            }
                                        }
                                    } else if (initHighlightedRow < rowIndex) {
                                        Log.d("Tag", "Forwards")
                                        highlightedLetters = ""
                                        for (i in 0 until differenceRow + 1) {
                                            highlightedLetters += wordGrid[initHighlightedColumn][initHighlightedRow + i]
                                            val currentRow = view.getChildAt(initHighlightedColumn) as TableRow
                                            val currentCell = currentRow.getChildAt(initHighlightedRow + i) as TextView
                                            if (!currentCell.typeface.isBold) {
                                                currentCell.setTextColor(Color.parseColor(colors[colorCount]))
                                                currentCell.setTypeface(null, Typeface.BOLD)
                                                highlightedIndexes.add(
                                                    intArrayOf(
                                                        initHighlightedColumn,
                                                        initHighlightedRow + i
                                                    )
                                                )
                                            }
                                        }
                                    }
                                } else if (differenceRow == 0 && differenceColumn != 0) {
                                    Log.d("Tag", "UpDown")
                                    for (arr in highlightedIndexes) {
                                        val differenceY = arr[0] - columnIndex
                                        val differenceX = arr[1] - rowIndex
                                        if (differenceY == 0 || differenceX != 0) {
                                            val currentRow = view.getChildAt(arr[0]) as TableRow
                                            val currentCell = currentRow.getChildAt(arr[1]) as TextView
                                            currentCell.setTextColor(Color.parseColor("#808080"))
                                            currentCell.typeface =
                                                Typeface.create(currentCell.typeface, Typeface.NORMAL)
                                        }
                                    }
                                    if (initHighlightedColumn > columnIndex) {
                                        Log.d("Tag", "Backwards")
                                        highlightedLetters = ""
                                        for (i in 0 until differenceColumn + 1) {
                                            highlightedLetters += wordGrid[initHighlightedColumn - i][initHighlightedRow]
                                            val currentRow = view.getChildAt(initHighlightedColumn - i) as TableRow
                                            val currentCell = currentRow.getChildAt(initHighlightedRow) as TextView
                                            if (!currentCell.typeface.isBold) {
                                                currentCell.setTextColor(Color.parseColor(colors[colorCount]))
                                                currentCell.setTypeface(null, Typeface.BOLD)
                                                highlightedIndexes.add(
                                                    intArrayOf(
                                                        initHighlightedColumn - i,
                                                        initHighlightedRow
                                                    )
                                                )
                                            }
                                        }
                                    } else {
                                        Log.d("Tag", "Forwards")
                                        highlightedLetters = ""
                                        for (i in 0 until differenceColumn + 1) {
                                            highlightedLetters += wordGrid[initHighlightedColumn + i][initHighlightedRow]
                                            val currentRow = view.getChildAt(initHighlightedColumn + i) as TableRow
                                            val currentCell = currentRow.getChildAt(initHighlightedRow) as TextView
                                            if (!currentCell.typeface.isBold) {
                                                currentCell.setTextColor(Color.parseColor(colors[colorCount]))
                                                currentCell.setTypeface(null, Typeface.BOLD)
                                                highlightedIndexes.add(
                                                    intArrayOf(
                                                        initHighlightedColumn + i,
                                                        initHighlightedRow
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            highlightedMap.put(columnIndex.toString() + rowIndex.toString(), text)
                            Log.d(
                                "wow",
                                "Column: $columnIndex. Row: $rowIndex. Text: $text. HLetters: $highlightedLetters Init: $initHighlightedColumn"
                            )
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (words.contains(highlightedLetters)) {
                        if (highlightedIndexes.size > highlightedLetters.length) {
                            for (i in highlightedLetters.length until highlightedIndexes.size) {
                                val arr = highlightedIndexes[i]
                                val currentRow = view.getChildAt(arr[0]) as TableRow
                                val currentCell = currentRow.getChildAt(arr[1]) as TextView
                                currentCell.setTextColor(Color.parseColor("#808080"))
                                currentCell.typeface = Typeface.create(currentCell.typeface, Typeface.NORMAL)
                            }
                        }
                        val arr = wordMap[highlightedLetters]
                        val bankRow = wordBankLayout.getChildAt(arr!![0] + 1) as TableRow
                        val bankTV = bankRow.getChildAt(arr[1]) as TextView
                        bankTV.visibility = View.INVISIBLE
                        wordMap.remove(highlightedLetters)
                        if (wordMap.isEmpty()) {
                            winner.visibility = View.VISIBLE
                        }
                    } else {
                        for (arr in highlightedIndexes) {
                            val currentRow = view.getChildAt(arr[0]) as TableRow
                            val currentCell = currentRow.getChildAt(arr[1]) as TextView
                            currentCell.setTextColor(Color.parseColor("#808080"))
                            currentCell.typeface = Typeface.create(currentCell.typeface, Typeface.NORMAL)
                        }
                    }
                    highlightedIndexes.clear()
                    lastHighlightedColumn = -1
                    lastHighlightedRow = -1
                    initHighlightedRow = -1
                    initHighlightedColumn = -1
                    highlightedLetters = ""
                    highlightedMap.clear()
                    highlightedCounter = 0
                    colorCount++
                    if (colorCount == colors.size - 1) {
                        colorCount = 0
                    }
                    Log.d(
                        "wow",
                        "Column: $lastHighlightedColumn. Row: $lastHighlightedRow. HLetters: $highlightedLetters"
                    )
                }
            }
            return@OnTouchListener true
        })
    }

    private fun findLocations() {
//        val words = arrayOf("SWIFT", "KOTLIN", "JAVA", "MOBILE", "NICE", "WOWZA", "HAHAHA")
        val diagThreshold = rowSize * 0.7
        for (word in words) {
            val randomBackwards = (0..1).random()
//            val randomBackwards = 0
            var randomDirection = (0..2).random()
            if (word.length > diagThreshold) {
                randomDirection = (0..1).random()
            }
//            val randomDirection = 2
            val isBackwards = randomBackwards == 0
            when (randomDirection) {
                0 -> // vertically
                    placeVertically(isBackwards, word)
                1 -> // horizontal placement
                    placeHorizontally(isBackwards, word)
                else -> placeDiagonally(isBackwards, word)
            }
        }
        for (i in 0 until rowSize) {
            for (j in 0 until columnSize) {
                if (wordGrid[i][j] == "") {
                    val randomAlphabet = (0 until 26).random()
                    wordGrid[i][j] = alphabet[randomAlphabet]
                }
            }
        }
    }

    private fun placeDiagonally(backwards: Boolean, word: String) {
        // set up all available columns and rows you can start from
        val availableColumns = columnSize - word.length + 1
        val availableRows = rowSize - word.length + 1
        val initColumnIndexes = arrayListOf<Int>()
        val initRowIndexes = arrayListOf<Int>()
        if (backwards) {
            for (i in columnSize - 1 downTo (columnSize - availableColumns)) initColumnIndexes.add(i)
            for (i in rowSize - 1 downTo (rowSize - availableRows)) initRowIndexes.add(i)
        } else {
            for (i in 0 until availableColumns) initColumnIndexes.add(i)
            for (i in 0 until availableRows) initRowIndexes.add(i)
        }
        var foundPlacement = false
        while (!foundPlacement) {
            foundPlacement = false
            val randomColumn = (0 until initColumnIndexes.size).random()
            val randomRow = (0 until initRowIndexes.size).random()
            var initRowIndex = initRowIndexes[randomRow]
            var initColumnIndex = initColumnIndexes[randomColumn]
            for (i in 0 until word.length) {
                val letter = word.substring(i, i + 1)
                val letterOnGrid = wordGrid[initColumnIndex][initRowIndex]
                if (letterOnGrid != letter && letterOnGrid != "") {
                    val startingRow = initRowIndexes[randomRow]
                    val startingColumn = initColumnIndexes[randomColumn]
                    for (j in 0 until abs(startingRow - initRowIndex)) {
                        if (!backwards) {
                            wordGrid[startingColumn + j][startingRow + j] = ""
                        } else {
                            wordGrid[startingColumn - j][startingRow - j] = ""
                        }
                    }
                    break
                }
                if (i == 0 && letterOnGrid != "") {
                    break
                }
                wordGrid[initColumnIndex][initRowIndex] = letter
                if (backwards) {
                    initColumnIndex--
                    initRowIndex--
                } else {
                    initColumnIndex++
                    initRowIndex++
                }

                if (i == word.length - 1) {
                    foundPlacement = true
                }
            }
            if (!foundPlacement) {
                initColumnIndexes.removeAt(randomColumn)
                initRowIndexes.removeAt(randomRow)
                if (initColumnIndexes.size == 0 || initRowIndexes.size == 0) {
                    foundPlacement = true
                    if (backwards) {
                        for (i in columnSize - 1 downTo (columnSize - availableColumns)) initColumnIndexes.add(i)
                        for (i in rowSize - 1 downTo (rowSize - availableRows)) initRowIndexes.add(i)
                    } else {
                        for (i in 0 until availableColumns) initColumnIndexes.add(i)
                        for (i in 0 until availableRows) initRowIndexes.add(i)
                    }
                }
            }
        }
    }

    private fun placeHorizontally(backwards: Boolean, word: String) {
        val availableStarts = columnSize - word.length + 1
        val rowIndexes = arrayListOf<Int>()
        for (i in 0 until columnSize) {
            rowIndexes.add(i)
        }
        val initIndexes = arrayListOf<Int>()
        if (backwards) {
            for (i in rowSize - 1 downTo (rowSize - availableStarts)) initIndexes.add(i)
        } else {
            for (i in 0 until availableStarts) initIndexes.add(i)
        }
        var valid = false
        while (!valid) {
            valid = false
            var foundPlacement = false
            val randomRow = (0 until rowIndexes.size).random()
            val randomRowIndex = rowIndexes[randomRow]
            while (!foundPlacement) {
                if (initIndexes.size == 0) break
                foundPlacement = false
                val randomStartingColumn = (0 until initIndexes.size).random()
                var initIndex = initIndexes[randomStartingColumn]
                for (i in 0 until word.length) {
                    val letter = word.substring(i, i + 1)
                    val letterOnGrid = wordGrid[randomRowIndex][initIndex]
                    if (letterOnGrid != letter && letterOnGrid != "") {
                        val startingIndex = initIndexes[randomStartingColumn]
                        for (j in 0 until abs(startingIndex - initIndex)) {
                            if (!backwards) {
                                wordGrid[randomRowIndex][startingIndex + j] = ""
                            } else {
                                wordGrid[randomRowIndex][startingIndex - j] = ""
                            }
                        }
                        break
                    }
                    if (i == 0 && letterOnGrid != "") {
                        break
                    }
                    wordGrid[randomRowIndex][initIndex] = letter
                    if (backwards) {
                        initIndex--
                    } else {
                        initIndex++
                    }
                    if (i == word.length - 1) {
                        foundPlacement = true
                        valid = true
                    }
                }
                if (!foundPlacement) {
                    initIndexes.removeAt(randomStartingColumn)
                    if (initIndexes.size == 0) {
                        foundPlacement = true
                        if (backwards) {
                            for (i in rowSize - 1 downTo (rowSize - availableStarts)) initIndexes.add(i)
                        } else {
                            for (i in 0 until availableStarts) initIndexes.add(i)
                        }
                    }
                }
            }
            rowIndexes.removeAt(randomRow)
        }
    }

    private fun placeVertically(backwards: Boolean, word: String) {
        val availableStarts = rowSize - word.length + 1
        val columnIndexes = arrayListOf<Int>()
        for (i in 0 until columnSize) {
            columnIndexes.add(i)
        }
        val initIndexes = arrayListOf<Int>()
        if (backwards) {
            for (i in columnSize - 1 downTo (columnSize - availableStarts))
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
                        val startingIndex = initIndexes[randomStartingRow]
                        for (j in 0 until abs(startingIndex - initIndex)) {
                            if (!backwards) {
                                wordGrid[startingIndex + j][randomColumnIndex] = ""
                            } else {
                                wordGrid[startingIndex - j][randomColumnIndex] = ""
                            }
                        }
                        break
                    }
                    if (i == 0 && letterOnGrid != "") {
                        break
                    }
                    // no problems add to the grid
                    wordGrid[initIndex][randomColumnIndex] = letter
                    if (backwards) {
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
                        if (backwards) {
                            for (i in columnSize - 1 downTo (columnSize - availableStarts))
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
