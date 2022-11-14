package com.example.unilocal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.unilocal.fragments.ImageFragment

class ImagesViewPager (var fragment: FragmentActivity, private var imagenes:ArrayList<String>): FragmentStateAdapter(fragment) {
    override fun getItemCount() = imagenes.size

    override fun createFragment(position: Int): Fragment {

        when(position){
            position -> return ImageFragment.newInstance(imagenes[position])
        }
        return Fragment()

    }
}