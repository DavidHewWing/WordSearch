package com.example.wordsearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupViewPager()
    }

    private fun initViews() {
        tabs = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.viewpager)
    }

    private fun setupViewPager() {

        val adapter = MyFragmentPagerAdapter(getSupportFragmentManager())

        var firstFragmet: MyFragment = MyFragment.newInstance("First Fragment")
        var secondFragmet: MyFragment = MyFragment.newInstance("Second Fragment")
        var thirdFragmet: MyFragment = MyFragment.newInstance("Third Fragment")

        adapter.addFragment(firstFragmet, "ONE")
        adapter.addFragment(secondFragmet, "TWO")
        adapter.addFragment(thirdFragmet, "THREE")

        viewPager!!.adapter = adapter

        tabs!!.setupWithViewPager(viewPager)
    }

}