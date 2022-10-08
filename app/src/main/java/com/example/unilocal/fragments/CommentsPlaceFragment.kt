package com.example.unilocal.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.adapter.CommentAdapter
import com.example.unilocal.bd.Comments
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.FragmentCommentsPlaceBinding
import com.example.unilocal.databinding.FragmentInfoPlaceBinding
import com.example.unilocal.models.Comment
import com.google.android.material.snackbar.Snackbar

class CommentsPlaceFragment : Fragment() {
    lateinit var binding: FragmentCommentsPlaceBinding
    var comments:ArrayList<Comment> = ArrayList()
    var codigoLugar: Int = 0
    lateinit var adapter: CommentAdapter
    var codeUser:Int = 0
    private var colorPorDefecto:Int = 0
    var estrellas: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            codigoLugar = requireArguments().getInt("id_lugar")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentsPlaceBinding.inflate(inflater,container,false)
        colorPorDefecto = binding.s1.textColors.defaultColor
        comments = Comments.lista(codigoLugar)
        if(comments.size == 0){
            binding.sinComentarios.visibility =View.VISIBLE
        }
        adapter = CommentAdapter(comments)
        binding.listaComentarios.adapter= adapter
        binding.listaComentarios.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        val sp = requireActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE)
        codeUser = sp.getInt("id",0)
        val place = Places.obtener(codigoLugar)

        binding.btnEnviar.setOnClickListener { makeComment() }

        for(i in 0 until binding.listStars.childCount){
            (binding.listStars[i] as TextView).setOnClickListener { presionarEstrella(i)}
        }
        return binding.root
    }

    fun makeComment(){
        binding.sinComentarios.visibility = View.GONE
        val text = binding.messageComment.text.toString()
        if(text.isNotEmpty() && estrellas > 0 ){
            val comentario = Comment(text,codeUser,codigoLugar,estrellas)
           Comments.crearComentario(comentario)
           limpiarFormulario()
           Snackbar.make(binding.root,"Se ha enviado el comentario",Snackbar.LENGTH_LONG).show()

            comments.add(comentario)
            adapter.notifyItemInserted(comments.size-1)

        }else{
            Snackbar.make(binding.root,"Debe escribir el comentario y seleccionar las estrellas",Snackbar.LENGTH_LONG).show()
        }
    }

    private fun limpiarFormulario(){
        binding.messageComment.setText("")
        borrarSeleccion()
        estrellas = 0
    }

    private fun presionarEstrella(pos:Int){
        estrellas=pos+1
        borrarSeleccion()
        for(i in 0..pos){
            (binding.listStars[i] as TextView).setTextColor(ContextCompat.getColor(requireContext(),R.color.yellow))
        }

    }

    private fun borrarSeleccion(){
        for(i in 0 until binding.listStars.childCount){
            (binding.listStars[i] as TextView).setTextColor(colorPorDefecto)
        }
    }

    companion object{
        fun newInstance(codigoLugar: Int): CommentsPlaceFragment{
            val args = Bundle()
            args.putInt("id_lugar",codigoLugar)
            val fragment = CommentsPlaceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}