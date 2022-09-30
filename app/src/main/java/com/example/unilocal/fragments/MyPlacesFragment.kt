package com.example.unilocal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.unilocal.R
import com.example.unilocal.databinding.FragmentInicioBinding
import com.example.unilocal.databinding.FragmentMyPlacesBinding


class MyPlacesFragment : Fragment() {
    lateinit var binding: FragmentMyPlacesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPlacesBinding.inflate(inflater,container,false)
        return binding.root
    }
}