package com.example.unilocal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.databinding.FragmentImageBinding

class ImageFragment : Fragment() {

    private var param1: String? = null
    private lateinit var binding:FragmentImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("url_image")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImageBinding.inflate(inflater, container, false)

        Glide.with( this )
            .load(param1)
            .into(binding.urlImage)

        return binding.root
    }

    companion object {
       fun newInstance(param1: String) =
                ImageFragment().apply {
                    arguments = Bundle().apply {
                        putString("url_image",param1)
                    }
                }
    }
}