package com.example.simaintegrationkotlin.common.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val _fragments: MutableList<Fragment> = arrayListOf()
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    val fragments: List<Fragment>
        get() = _fragments

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Fragment>) {
        _fragments.clear()
        _fragments.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = _fragments.size

    override fun createFragment(position: Int) = _fragments[position]
}