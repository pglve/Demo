/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.test

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.pglvee.base.viewBinding.viewBinding
import me.pglvee.test.databinding.ActivityTestTestBinding

class TestActivity: AppCompatActivity() {

    private val viewBinding by viewBinding(ActivityTestTestBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_test)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val testTxts = arrayOf("左右", "上下")

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                return TestFragment(position)
            }
        }
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = testTxts[position]
        }.attach()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("Touch", "activity :: dispatchTouchEvent ${ev?.action}")
        return super.dispatchTouchEvent(ev)
    }
}