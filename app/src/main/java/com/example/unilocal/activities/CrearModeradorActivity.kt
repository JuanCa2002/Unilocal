package com.example.unilocal.activities


import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.unilocal.R
import com.example.unilocal.bd.Cities
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityCrearModeradorBinding
import com.example.unilocal.models.City
import com.example.unilocal.models.Rol
import com.example.unilocal.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CrearModeradorActivity : AppCompatActivity() {
    lateinit var binding: ActivityCrearModeradorBinding
    var cityPosition: Int = -1
    lateinit var cities: ArrayList<City>
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearModeradorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cities = ArrayList()
        loadCities()
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()
        binding.btnRegistro.setOnClickListener { createModerator() }
    }

    fun loadCities(){
        cities.clear()
        Firebase.firestore
            .collection("citiesF")
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    val city = doc.toObject(City::class.java)
                    city.key = doc.id
                    cities.add(city)
                }
                var adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,cities)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.cityModerator.adapter= adapter
                binding.cityModerator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        cityPosition = p2
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
    }

    fun createModerator(){
        val nombre = binding.moderatorName.text.toString()
        val nickname = binding.nicknameModerator.text.toString()
        val correo = binding.moderatorEmail.text.toString()
        val password = binding.moderatorPassword.text.toString()
        val confirmPassword = binding.moderatorPasswordConfirm.text.toString()
        val idCity = cities[cityPosition].key

        if(nombre.isEmpty()){
            binding.nameLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.nameLayout.error = null
        }

        if(nickname.isEmpty()){
            binding.nicknameLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.nicknameLayout.error = null
        }

        if(correo.isEmpty()){
            binding.emailLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.emailLayout.error = null
        }

        if(password.isEmpty()){
            binding.passwordLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.passwordLayout.error = null
        }

        if(confirmPassword.isEmpty()){
            binding.confirmPasswordLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.confirmPasswordLayout.error = null
        }

        if(confirmPassword != password){
            binding.confirmPasswordLayout.error = getString(R.string.txt_contrasenas_incorrectas)
            binding.passwordLayout.error = getString(R.string.txt_contrasenas_incorrectas)
        }

        if(nombre.isNotEmpty() && nickname.isNotEmpty() && correo.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword && idCity != ""){
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(correo,password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        if(user != null){
                            verificarEmail(user)
                            val userRegister = User( nombre, nickname, correo ,idCity, Rol.MODERATOR)
                            Firebase.firestore
                                .collection("users")
                                .document(user.uid)
                                .set(userRegister)
                                .addOnSuccessListener {
                                    Snackbar.make(binding.root,R.string.registro_exitoso, Snackbar.LENGTH_LONG).show()
                                    setDialog(false)
                                    val intent = Intent(this, GestionModeratorActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }
                    }

                }.addOnFailureListener {
                    setDialog(false)
                    Snackbar.make(binding.root,it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
        }else{
            setDialog(false)
        }
    }

    private fun verificarEmail(user: FirebaseUser){
        user.sendEmailVerification().addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(baseContext, R.string.email_enviado, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(baseContext, R.string.email_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }

}