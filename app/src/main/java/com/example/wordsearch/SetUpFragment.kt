package com.example.wordsearch

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_set_up.*

class SetUpFragment : Fragment() {

    private var gameData: GameData? = null
    private var loaded = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // grab the gamedata
        activity?.let {
            gameData = ViewModelProviders.of(it).get(GameData::class.java)
        }

        // add all initial values
        val list = ArrayList<Int>()
        val wordList = ArrayList<String>()
        wordList.add("SWIFT")
        wordList.add("KOTLIN")
        wordList.add("OBJECTIVEC")
        wordList.add("VARIABLE")
        wordList.add("JAVA")
        wordList.add("MOBILE")
        val wordAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, wordList)
        wordLV.adapter = wordAdapter

        list.add(10)
        list.add(11)
        list.add(12)
        list.add(13)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sizeSpinner.adapter = adapter
        sizeSpinner.setSelection(2)

        // when you click you send values to the play fragment
        startButton.setOnClickListener { v ->
            val pager = (activity as MainActivity).getViewPager()
            pager.adapter!!.notifyDataSetChanged()
            val spinnerVal = sizeSpinner.selectedItem.toString().toInt()
            val map = HashMap<String, Any>()
            wordList.sortByDescending { it.length }
            Log.d("Tag", wordList.toString())
            map["words"] = wordList
            map["rowSize"] = spinnerVal
            map["columnSize"] = spinnerVal
            loaded++
            map["loaded"] = loaded
            gameData?.data?.postValue(map)
            (activity as MainActivity).getViewPager().currentItem = 0
        }

        wordLV.setOnItemClickListener { parent, view, position, id ->
            wordList.removeAt(position)
            wordAdapter.notifyDataSetChanged()
        }

        // adding words to the view
        // checks if you have if you have too many words for the grid size
        addButton.setOnClickListener { v ->
            val word = wordET.text.toString()
            val spinnerVal = sizeSpinner.selectedItem.toString().toInt()
            addButton.hideKeyboard()
            // if your word is bigger than the grid
            if (word.length > spinnerVal) {
                Toast.makeText(context, "Word length must be smaller than the grid size.", Toast.LENGTH_SHORT).show()
                wordET.setText("")
                // if your word is too small
            } else if (word.length < 3) {
                Toast.makeText(context, "Word length must be must be greater than 3", Toast.LENGTH_SHORT).show()
                wordET.setText("")
                //  if your word is ""
            } else if (word.isEmpty()) {
                Toast.makeText(context, "Empty string not allowed.", Toast.LENGTH_SHORT).show()
                wordET.setText("")
                // if you word contains non-alphabetical things
            } else if (!isAlpha(word)) {
                Toast.makeText(context, "String needs to have letters only.", Toast.LENGTH_SHORT).show()
                wordET.setText("")
            } else if (spinnerVal == 10 && wordList.size >= 8) {
                Toast.makeText(context, "Max words for grid size of 10 is 8.", Toast.LENGTH_SHORT).show()
                wordET.setText("")
            } else if (spinnerVal == 11 && wordList.size >= 10) {
                Toast.makeText(context, "Max words for grid size of 11 is 10", Toast.LENGTH_SHORT).show()
                wordET.setText("")
            } else if (spinnerVal == 12 && wordList.size >= 12) {
                Toast.makeText(context, "Max words for grid size of 12 is 12", Toast.LENGTH_SHORT).show()
                wordET.setText("")
            } else if (spinnerVal == 13 && wordList.size >= 12) {
                Toast.makeText(context, "Max words for grid size of 12 is 12", Toast.LENGTH_SHORT).show()
                wordET.setText("")
            } else {
                wordET.setText("")
                wordList.add(word.toUpperCase())
                wordAdapter.notifyDataSetChanged()
            }

        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance() =
            SetUpFragment().apply {
            }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun isAlpha(name: String): Boolean {
        val chars = name.toCharArray()

        for (c in chars) {
            if (!Character.isLetter(c)) {
                return false
            }
        }

        return true
    }
}
