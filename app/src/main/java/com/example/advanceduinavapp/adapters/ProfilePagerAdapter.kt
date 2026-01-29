package com.example.advanceduinavapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.advanceduinavapp.fragments.GalleryFragment
import com.example.advanceduinavapp.fragments.InfoFragment

class ProfilePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        private val TAB_TITLES = arrayOf("Info", "Gallery")
    }

    override fun getItemCount(): Int = TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InfoFragment()
            1 -> GalleryFragment()
            else -> InfoFragment()
        }
    }

    fun getTabTitle(position: Int): String {
        return TAB_TITLES[position]
    }
}
