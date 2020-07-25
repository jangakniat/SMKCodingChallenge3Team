package com.pedolu.smkcodingchallenge3team.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pedolu.smkcodingchallenge3team.fragment.CountrySummaryFragment
import com.pedolu.smkcodingchallenge3team.fragment.GlobalFragment
import com.pedolu.smkcodingchallenge3team.fragment.IndonesiaFragment
import com.pedolu.smkcodingchallenge3team.fragment.VolunteerFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val menuSize = 4
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                GlobalFragment()
            }
            1 -> {
                CountrySummaryFragment()
            }
            2 -> {
                IndonesiaFragment()
            }
            3 -> {
                VolunteerFragment()
            }
            else -> {
                GlobalFragment()
            }
        }
    }

    override fun getItemCount() = menuSize
}