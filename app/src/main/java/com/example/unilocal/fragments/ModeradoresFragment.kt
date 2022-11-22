package com.example.unilocal.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.activities.CrearModeradorActivity
import com.example.unilocal.activities.GestionModeratorActivity
import com.example.unilocal.activities.ModeratorActivity
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.databinding.FragmentModeradoresBinding
import com.example.unilocal.models.Rol
import com.example.unilocal.models.StatusUser
import com.example.unilocal.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ModeradoresFragment : Fragment() {
    lateinit var binding:FragmentModeradoresBinding
    lateinit var moderators : ArrayList<User>
    lateinit var adapter: ModeratorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModeradoresBinding.inflate(inflater, container, false)
        moderators = ArrayList()
        adapter = ModeratorAdapter(moderators, requireContext())
        binding.listModerators.adapter = adapter
        binding.listModerators.layoutManager  = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding.btnCreateModerator.setOnClickListener { irCrearModerator() }
        return binding.root
    }

    fun irCrearModerator(){
        val intent = Intent(requireContext(), CrearModeradorActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        moderators.clear()
        val estadoConexion = (requireActivity()as GestionModeratorActivity).estadoConexion
        if(estadoConexion){
            Firebase.firestore
                .collection("users")
                .whereEqualTo("rol", Rol.MODERATOR)
                .whereEqualTo("status", StatusUser.HABILITADO)
                .get()
                .addOnSuccessListener {
                    for(doc in it){
                        val moderator = doc.toObject(User::class.java)
                        moderator.key = doc.id
                        moderators.add(moderator)
                    }
                    adapter.notifyDataSetChanged()
                }
        }else{
            Snackbar.make(binding.root, getString(R.string.no_cargar_apartado), Snackbar.LENGTH_LONG).show()
        }
    }


}