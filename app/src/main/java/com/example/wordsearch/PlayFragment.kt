package com.example.wordsearch

import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import kotlinx.android.synthetic.main.fragment_play.*

class PlayFragment : Fragment() {

    private val alphabet: List<Char> = mutableListOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
    private val columnSize = 10
    private val rowSize = 10
    private var wordLayoutWidth = 0
    private var wordLayoutHeight = 0
    private var table: TableLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayouts()
    }

    private fun initLayouts(){
        val viewTreeObserver: ViewTreeObserver = parentLayout.viewTreeObserver
        if(viewTreeObserver.isAlive) {
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

    private fun setupTable () {
        val width = wordLayoutWidth / this.rowSize
        val height = wordLayoutHeight / this.columnSize
        for(i in 0 until this.rowSize + 1) {
            val row = TableRow(context)
            row.layoutParams
            row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            for(j in 0 until this.columnSize) {
                val button = Button(context)
                button.apply {
                    layoutParams = TableRow.LayoutParams(width, height, 1.0F)
                    text = "R $i C $j"
                }
                row.addView(button)
            }
            wordLayout?.addView(row)
        }
    }

    companion object {
        fun newInstance() =
            PlayFragment().apply {

            }
    }
}
