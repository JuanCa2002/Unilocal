package com.example.unilocal.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CommentsPlaceFragment : Fragment() {
    lateinit var binding: FragmentCommentsPlaceBinding
    lateinit var adapter: CommentAdapter
    private var colorPorDefecto:Int = 0
    var comments:ArrayList<Comment> = ArrayList()
    var codigoLugar: String? = ""
    var estrellas: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            codigoLugar = requireArguments().getString("id_lugar")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentsPlaceBinding.inflate(inflater,container,false)
        colorPorDefecto = binding.s1.textColors.defaultColor
        Firebase.firestore
            .collection("placesF")
            .document(codigoLugar!!)
            .collection("commentsF")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val comment = doc.toObject(Comment::class.java)
                    if(comment!=null){
                        comment.key = doc.id
                        comments.add(comment)
                    }
                }
                if(comments.size == 0){
                    binding.sinComentarios.visibility = View.VISIBLE
                }
                adapter = CommentAdapter(comments)
                binding.listaComentarios.adapter= adapter
                binding.listaComentarios.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

            }.addOnFailureListener {
               Log.e("Error en los mensajes",it.message.toString())
            }
        for(i in 0 until binding.listStars.childCount){
            (binding.listStars[i] as TextView).setOnClickListener { presionarEstrella(i)}
        }
        binding.btnEnviar.setOnClickListener { makeComment() }

        return binding.root
    }

    fun makeComment(){
        binding.sinComentarios.visibility = View.GONE
        val text = binding.messageComment.text.toString()
        if(text.isNotEmpty() && estrellas > 0 ) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val comentario = Comment(text, user.uid, estrellas)
                Firebase.firestore
                    .collection("placesF")
                    .document(codigoLugar!!)
                    .collection("commentsF")
                    .add(comentario)
                    .addOnSuccessListener {
                        limpiarFormulario()
                        Snackbar.make(
                            binding.root,
                            R.string.txt_comentario_enviado,
                            Snackbar.LENGTH_LONG
                        ).show()
                        comments.add(comentario)
                        adapter.notifyItemInserted(comments.size - 1)
                    }.addOnFailureListener {
                        Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG).show()
                    }
             }
        }else{
            Snackbar.make(binding.root,R.string.txt_advertencia_comentarios,Snackbar.LENGTH_LONG).show()
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
        fun newInstance(codigoLugar: String): CommentsPlaceFragment{
            val args = Bundle()
            args.putString("id_lugar",codigoLugar)
            val fragment = CommentsPlaceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}