package com.example.unilocal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.unilocal.R
import com.example.unilocal.adapter.ViewPageModeratorAdapter
import com.example.unilocal.adapter.ViewPagerAdapter
import com.example.unilocal.databinding.FragmentPendientesPlaceBinding
import com.example.unilocal.databinding.FragmentRegistreBinding
import com.google.android.material.tabs.TabLayoutMediator

class RegistreFragment : Fragment() {
    lateinit var binding: FragmentRegistreBinding
    var codeModerator:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            codeModerator = requireArguments().getInt("code_moderator")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistreBinding.inflate(inflater,container,false)
        binding.viewPaper.adapter = ViewPageModeratorAdapter(requireActivity(),codeModerator)
        TabLayoutMediator(binding.tabs, binding.viewPaper){tab, pos ->
            when(pos){
                0 -> tab.text = getString(R.string.txt_aceptados)
                1 -> tab.text = getString(R.string.txt_rechazados)
            }
        }.attach()
        return binding.root
    }

    companion object{
        fun newInstance(codeModerator: String?):RegistreFragment{
            val args = Bundle()
            args.putString("code_moderator",codeModerator)
            val fragment = RegistreFragment()
            fragment.arguments = args
            return fragment

        }
    }
}