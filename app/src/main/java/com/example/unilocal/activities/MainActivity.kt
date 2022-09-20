package com.example.unilocal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.unilocal.R
import com.example.unilocal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtBusqueda.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEARCH){
                val textSearch = binding.txtBusqueda.text.toString()
                if(textSearch.isNotEmpty()){
                    val intent = Intent(baseContext, ResultadoBusquedaActivity::class.java)
                    intent.putExtra("txt_search",textSearch)
                    startActivity(intent)
                }
            }
            true
        }
    }

    fun irCrearLugar(view:View){
        val intent = Intent(this, CrearLugarActivity::class.java)
        startActivity(intent)
    }

    fun irLogin(view:View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}