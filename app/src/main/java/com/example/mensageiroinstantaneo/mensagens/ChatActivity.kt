package com.example.mensageiroinstantaneo.mensagens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.mensageiroinstantaneo.NovaMensagemActivity
import com.example.mensageiroinstantaneo.R
import com.example.mensageiroinstantaneo.modelo.ChatDTO
import com.example.mensageiroinstantaneo.modelo.UsuarioDTO
import com.example.mensageiroinstantaneo.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatActivity : Utils() {
    lateinit var campoMensagem : EditText
    lateinit var usuario : UsuarioDTO
    lateinit var usuarioLogadoId : String
    lateinit var recycler : RecyclerView
    val adapter = GroupieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        recycler = findViewById<RecyclerView>(R.id.recycler_chat)
        usuarioLogadoId = FirebaseAuth.getInstance().uid.toString()
        usuario = intent.getParcelableExtra(NovaMensagemActivity.USER_KEY)!!
        if (usuario != null) {
            supportActionBar?.title = usuario!!.username
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listenerNovasMensagens()

        val botao_enviar = findViewById<ImageButton>(R.id.enviar_mensagem)
        botao_enviar.setOnClickListener {
            enviaMensagem()
        }
    }

    private fun listenerNovasMensagens(){
        val ref = FirebaseDatabase.getInstance().getReference("/mensagens")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val mensagemChat = snapshot.getValue(ChatDTO::class.java)
                if (mensagemChat != null) {
                    if(mensagemChat.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(ItemChatDireita(mensagemChat.text))
                    }else{
                    adapter.add(ItemChatEsquerda(mensagemChat.text))
                    }
                }
                recycler.adapter = adapter
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun enviaMensagem(){
        campoMensagem = findViewById(R.id.campo_mensagem)
        var mensagem = campoMensagem.text.toString().trim()
        if(mensagem.replace("\\s/g".toRegex(), "").length == 0){
            return
        }
        if(usuarioLogadoId == null && usuario.uid == null ) {
            toast("Não foi possível enviar essa mensagem. \n Tente novamente mais tarde.")
            return
        }
        val database = FirebaseDatabase.getInstance().getReference("/mensagens").push()
        val chatDto = ChatDTO(database.key, mensagem, usuarioLogadoId, usuario.uid, System.currentTimeMillis() / 1000)
        database.setValue(chatDto)
        campoMensagem.text.clear()
    }
}

class ItemChatEsquerda(val text : String) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.linha_chat_esquerda
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.campo_linha_esquerda).text = text
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/mensageiroinstantaneo.appspot.com/o/imagens%2F2e7dfd01-6cf5-4be3-96f6-8c792e08a8af?alt=media&token=7078b144-5e34-4fe5-a3e3-22b1b6352cef")
            .into(viewHolder.itemView.findViewById<ImageView>(R.id.foto_chat_esquerda))
    }

}

class ItemChatDireita(val text : String) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.linha_chat_direita
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.campo_linha_direita).text = text
    }

}