package com.example.unilocal.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.unilocal.R
import com.example.unilocal.bd.Cities
import com.example.unilocal.databinding.ActivityRegistroBinding
import com.example.unilocal.models.City
import com.example.unilocal.models.Rol
import com.example.unilocal.models.User
import com.example.unilocal.sqlite.UniLocalDbHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class RegistroActivity : AppCompatActivity() {

    private lateinit var db:UniLocalDbHelper
    lateinit var binding: ActivityRegistroBinding
    lateinit var cities: ArrayList<City>
    lateinit var dialog: Dialog
    var cityPosition: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cities = ArrayList()
        loadCities()
        binding.btnRegistro.setOnClickListener{ registraUsuario()}

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()

        db = UniLocalDbHelper(this)
    }

    fun registraUsuario(){
        val name = binding.userName.text.toString()
        val nickname = binding.nicknameUsuario.text.toString()
        val email = binding.emailUsuario.text.toString()
        val password = binding.userPassword.text.toString()
        val idCity = cities[cityPosition].key
        val confirmPassword = binding.userPasswordConfirm.text.toString()

        setDialog(true)

        if(confirmPassword.isEmpty()){
            binding.nameLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.nameLayout.error = null
        }

        if(name.isEmpty()){
            binding.nameLayout.error = getString(R.string.txt_obligatorio)
        }
        else{
            binding.nameLayout.error = null
        }

        if(nickname.isEmpty()){
            binding.nicknameLayout.error = getString(R.string.txt_obligatorio)
        }
        else{
            if (nickname.length >10){
                binding.nicknameUsuario.error = getString(R.string.txt_maximo_caracteres)
            }
            else {
                binding.nicknameLayout.error = null
            }
        }

        if(email.isEmpty()){
            binding.emailLayout.error = getString(R.string.txt_obligatorio)
        }
        else{
            binding.emailLayout.error = null
        }

        if(password.isEmpty()){
            binding.passwordLayout.error = getString(R.string.txt_obligatorio)
        }
        else{
            binding.passwordLayout.error = null
        }

        if(name.isNotEmpty() && email.isNotEmpty() && confirmPassword.isNotEmpty() && nickname.isNotEmpty() && nickname.length<=15 && password.isNotEmpty() && idCity!= ""){
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        if(user != null){
                            verificarEmail(user)
                            val userRegister = User( name, nickname, email ,idCity, Rol.USER)
                            userRegister.imageReference = "p-1668873917292.jpg"
                            userRegister.imageUri = "https://firebasestorage.googleapis.com/v0/b/unilocal-eam-e8def.appspot.com/o/p-1668873917292.jpg?alt=media&token=e2713363-3420-47fc-8155-c5d5a0370ce0"
                            Firebase.firestore
                                .collection("users")
                                .document(user.uid)
                                .set(userRegister)
                                .addOnSuccessListener {
                                    db.createUser(
                                        User(
                                            user.uid,
                                            userRegister!!.nombre,
                                            userRegister!!.nickname,
                                            userRegister!!.correo,
                                            userRegister!!.idCity
                                        )
                                    )
                                    Snackbar.make(binding.root,R.string.registro_exitoso, Snackbar.LENGTH_LONG).show()
                                    setDialog(false)
                                    startActivity(Intent(this, LoginActivity::class.java))
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

    fun loadCities(){
        Firebase.firestore
            .collection("citiesF")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val city = doc.toObject(City::class.java)
                    city.key = doc.id
                    cities.add(city)
                }
                var adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,cities)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.departmentPlace.adapter= adapter
                binding.departmentPlace.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        cityPosition = p2
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
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