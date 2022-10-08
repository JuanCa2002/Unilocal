package com.example.unilocal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.unilocal.fragments.CommentsPlaceFragment
import com.example.unilocal.fragments.InfoPlaceFragment

class ViewPagerAdapter(var fragment:FragmentActivity, var codigoLugar:Int):FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> InfoPlaceFragment.newInstance(codigoLugar)
            else -> CommentsPlaceFragment.newInstance(codigoLugar)
        }
    }
}