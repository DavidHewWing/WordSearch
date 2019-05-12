package com.example.wordsearch

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class MyViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        if(position== 0) {
            return PlayFragment.newInstance()
        } else if (position == 1){
            return SetUpFragment.newInstance()
        }
        return SetUpFragment.newInstance()
    }

    override fun getCount(): Int {
        return 2
    }

}