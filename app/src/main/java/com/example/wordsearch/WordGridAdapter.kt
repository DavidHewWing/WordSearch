package com.example.wordsearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.word_grid_item.view.*

class WordGridAdapter: BaseAdapter {

    var context: Context? = null
    var letterList = ArrayList<Char>()

    constructor(context: Context, letterList: ArrayList<Char>): super() {
        this.context = context
        this.letterList = letterList
    }

    override fun getCount(): Int {
        return letterList.size
    }

    override fun getItem(position: Int): Any {
        return letterList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val letter = this.letterList[position].toString()

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val letterView = inflater.inflate(R.layout.word_grid_item, null)
        letterView.wordGridItemTV.text = letter!!

        return letterView

    }

}