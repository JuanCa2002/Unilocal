package com.example.unilocal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ToggleButton
import com.example.unilocal.R
import com.example.unilocal.databinding.ActivityRegistroBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class RegistroActivity : AppCompatActivity() {

    lateinit var binding:ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.botonToggle.setOnClickListener {escucharEventoToggle()}

        binding.botonSwitch.setOnClickListener {escucharEventoSwitch()}

        binding.btnImage.setOnClickListener{escucharEventoImage()}

        binding.botonFlotante.setOnClickListener{escucharEventoFlotante()}

        binding.botonFlotanteExtend.setOnClickListener{escucharEventoFlotanteExtend()}
    }

    fun escucharEventoToggle(){
        if(binding.botonToggle.isChecked){
            mostrarMensaje(binding.botonToggle,"Boton activo")
        }
        else{
            mostrarMensaje(binding.botonToggle,"Boton inactivo")
        }
    }

    fun escucharEventoSwitch(){
        if(binding.botonSwitch.isChecked){
            mostrarMensaje(binding.botonSwitch,"Boton switch activo")
        }
        else{
            mostrarMensaje(binding.botonToggle,"Boton switch inactivo")
        }
    }

    fun escucharEventoImage(){
        mostrarMensaje(binding.btnImage, "Boton de la imagen")
    }

    fun escucharEventoFlotante(){
        mostrarMensaje(binding.botonFlotante, "Boton flotante ")
    }

    fun escucharEventoFlotanteExtend(){
        mostrarMensaje(binding.botonFlotanteExtend, "Boton flotante con texto")
    }

    fun mostrarMensaje(view: View, mensaje:String){
        Snackbar.make(this, view, mensaje, Snackbar.LENGTH_LONG ).show()
    }
}