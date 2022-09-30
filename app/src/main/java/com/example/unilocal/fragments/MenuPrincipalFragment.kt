package com.example.unilocal.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.example.unilocal.R
import com.example.unilocal.activities.ResultadoBusquedaActivity
import com.example.unilocal.databinding.FragmentMenuPrincipalBinding

class MenuPrincipalFragment : Fragment() {
    lateinit var binding:FragmentMenuPrincipalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuPrincipalBinding.inflate(inflater,container,false)

        binding.txtBusqueda.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEARCH){
                val textSearch = binding.txtBusqueda.text.toString()
                if(textSearch.isNotEmpty()){
                    val intent = Intent(activity, ResultadoBusquedaActivity::class.java)
                    intent.putExtra("txt_search",textSearch)
                    startActivity(intent)
                }
            }
            true
        }

        return binding.root
    }
}