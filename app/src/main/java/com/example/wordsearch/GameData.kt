package com.example.wordsearch

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.ClipData

class GameData : ViewModel() {
//    private var rowSize: Int = 0
//    private var columnSize: Int = 0
//    private var empty: Boolean = false
//    private var words:ArrayList<String> = arrayListOf("")
    val data = MutableLiveData<HashMap<String, Any>>()
}