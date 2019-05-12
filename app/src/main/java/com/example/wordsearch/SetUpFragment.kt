package com.example.wordsearch

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_set_up.*

class SetUpFragment : Fragment() {

    private var gameData: GameData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.let {
            gameData = ViewModelProviders.of(it).get(GameData::class.java)
        }

        val list = ArrayList<Int>()
        val wordList = ArrayList<String>()
        wordList.add("SWIFT")
        wordList.add("KOTLIN")
        wordList.add("OBJECTIVEC")
        wordList.add("VARIABLE")
        wordList.add("JAVA")
        wordList.add("MOBILE")
        val wordAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, wordList)
        wordLV.setAdapter(wordAdapter)

        list.add(10)
        list.add(11)
        list.add(12)
        list.add(13)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sizeSpinner.setAdapter(adapter)

        startButton.setOnClickListener { v ->
            val spinnerVal = sizeSpinner.selectedItem.toString().toInt()
            val map = HashMap<String, Any>()
            map["words"] = wordList
            map["rowSize"] = spinnerVal
            map["columnSize"] = spinnerVal
            gameData?.data?.postValue(map)
            (activity as MainActivity).getViewPager().setCurrentItem(0)
        }

        addButton.setOnClickListener { v ->
            val word = wordET.text.toString()
            val spinnerVal = sizeSpinner.selectedItem.toString().toInt()
            if(word.length > spinnerVal) {
                Toast.makeText(context, "Word length must be smaller than the grid size.", Toast.LENGTH_SHORT)
            }else if(word.length == 0){
                Toast.makeText(context, "Empty string not allowed.", Toast.LENGTH_SHORT)
            }else {
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
}
