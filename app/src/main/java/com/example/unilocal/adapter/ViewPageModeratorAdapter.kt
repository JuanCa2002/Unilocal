package com.example.unilocal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.unilocal.fragments.AceptadosFragment
import com.example.unilocal.fragments.CommentsPlaceFragment
import com.example.unilocal.fragments.InfoPlaceFragment
import com.example.unilocal.fragments.RechazadosFragment

class ViewPageModeratorAdapter(var fragment: FragmentActivity, var codeMoredator:Int): FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AceptadosFragment.newInstance(codeMoredator)
            else -> RechazadosFragment.newInstance(codeMoredator)
        }
    }
}