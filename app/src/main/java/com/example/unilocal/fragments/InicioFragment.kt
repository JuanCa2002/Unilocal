package com.example.unilocal.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.unilocal.R
import com.example.unilocal.activities.MainActivity
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.FragmentInicioBinding


class InicioFragment : Fragment() {

    lateinit var binding: FragmentInicioBinding
    var code:Int = -1
    var bundle:Bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioBinding.inflate(inflater,container,false)
        code = this.bundle.getInt("code")

        return binding.root
    }

}