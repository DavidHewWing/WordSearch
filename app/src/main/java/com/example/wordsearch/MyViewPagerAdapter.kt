package com.example.wordsearch

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log


class MyViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return PlayFragment.newInstance()
        } else if (position == 1) {
            return SetUpFragment.newInstance()
        }
        return SetUpFragment.newInstance()
    }

    override fun getCount(): Int {
        return 2
    }

    // if it is a playfragment then reset everything in the play fragment
    // called when notifyDataSetChanged()
    override fun getItemPosition(`object`: Any): Int {
        if (`object` is PlayFragment) {
            val f = `object`
            f.update()
            Log.d("hawhaw", "itemPosition")
        }
        return super.getItemPosition(`object`)
    }

}