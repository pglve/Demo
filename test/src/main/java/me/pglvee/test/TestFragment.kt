/*
 * Copyright (c) 2021. pinggonglve
 */

package me.pglvee.test

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class TestFragment(val position: Int): Fragment(R.layout.fragment_test_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listRv = view.findViewById<RecyclerView>(R.id.listRv)

        val listData1 = mutableListOf("AAF", "AVF", "BVF", "BBG", "DGH", "DFG", "EFF", "AAF", "AVF", "BVF", "BBG",
            "DGH", "DFG", "EFF", "AAF", "AVF", "BVF", "BBG", "DGH", "DFG", "EFF", "AAF", "AVF", "BVF", "BBG", "DGH", "DFG", "EFF", "AAF", "AVF", "BVF", "BBG", "DGH", "DFG", "EFF")

        val listData2 = mutableListOf("BAF", "BVF", "CVF", "CBG", "EGH", "EFG", "FFF", "FAF", "FVF", "FVF", "FBG",
            "DGH", "DFG", "EFF", "AAF", "AVF", "BVF", "BBG", "DGH", "DFG", "EFF", "AAF", "AVF", "BVF", "BBG", "DGH", "DFG", "EFF", "AAF", "AVF", "BVF", "BBG", "DGH", "DFG", "EFF")

        listRv.layoutManager = LinearLayoutManager(context)
        listRv.adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_test_test, if(position == 1) listData2 else listData1) {
            override fun convert(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.txtTv, item)
            }

        }
    }
}