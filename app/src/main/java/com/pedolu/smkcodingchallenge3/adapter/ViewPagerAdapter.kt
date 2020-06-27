package com.pedolu.smkcodingchallenge3.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pedolu.smkcodingchallenge3.fragment.GlobalFragment
import com.pedolu.smkcodingchallenge3.fragment.GlobalStatusFragment
import com.pedolu.smkcodingchallenge3.fragment.IndonesiaFragment
import com.pedolu.smkcodingchallenge3.fragment.LocalFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val menuSize = 4
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                GlobalFragment()
            }
            1 -> {
                LocalFragment()
            }
            2 -> {
                IndonesiaFragment()
            }
            3 -> {
                GlobalStatusFragment()
            }
            else -> {
                GlobalFragment()
            }
        }
    }

    override fun getItemCount() = menuSize
}