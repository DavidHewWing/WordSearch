package com.example.wordsearch

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * this data is shared amongst each fragment
 */
class GameData : ViewModel() {
    val data = MutableLiveData<HashMap<String, Any>>()
}