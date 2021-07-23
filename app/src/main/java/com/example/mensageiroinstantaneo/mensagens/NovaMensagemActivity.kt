package com.example.mensageiroinstantaneo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mensageiroinstantaneo.mensagens.ChatActivity
import com.example.mensageiroinstantaneo.modelo.UsuarioDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NovaMensagemActivity : AppCompatActivity() {
    lateinit var recycler : RecyclerView

    companion object{
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_mensagem)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Selecione um usuário"

        buscaUsuarios()
    }

    private fun buscaUsuarios(){
        val database = FirebaseDatabase.getInstance().getReference("/usuarios")

        database.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                recycler = findViewById(R.id.recyclerview_usuarios)
                val adapter = GroupieAdapter()
                snapshot.children.forEach {
                    val user = it.getValue(UsuarioDTO::class.java)
                    if(user != null && user.uid != FirebaseAuth.getInstance().uid){
                    adapter.add(ItemUsuario(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val itemUsuario = item as ItemUsuario
                    val intent = Intent(view.context, ChatActivity::class.java)
                    intent.putExtra(USER_KEY, itemUsuario.usuario)
                    startActivity(intent)
                    finish()

                }
                recycler.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

class ItemUsuario(val usuario: UsuarioDTO) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.linha_usuario_nova_mensagem
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.nome_usuario_nova_mensagem).text = usuario.username
        Picasso.get().load(usuario.fotoPerfil).into(viewHolder.itemView
            .findViewById<ImageView>(R.id.imagem_usuario_nova_mensagem))
    }

}