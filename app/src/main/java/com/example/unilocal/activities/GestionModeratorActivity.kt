package com.example.unilocal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.bd.Moderators
import com.example.unilocal.databinding.ActivityGestionModeratorBinding
import com.example.unilocal.models.Moderator

class GestionModeratorActivity : AppCompatActivity() {
    lateinit var binding: ActivityGestionModeratorBinding
    lateinit var moderators : ArrayList<Moderator>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionModeratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moderators = Moderators.listar()

        val adapter = ModeratorAdapter(moderators)
        binding.listModerators.adapter = adapter
        binding.listModerators.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        Log.e("GestionModeratorActivity",moderators.toString())

    }
}