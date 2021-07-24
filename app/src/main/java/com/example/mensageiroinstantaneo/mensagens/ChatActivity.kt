package com.example.mensageiroinstantaneo.mensagens

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.mensageiroinstantaneo.NovaMensagemActivity
import com.example.mensageiroinstantaneo.R
import com.example.mensageiroinstantaneo.modelo.ChatDTO
import com.example.mensageiroinstantaneo.modelo.UsuarioDTO
import com.example.mensageiroinstantaneo.utils.Utils
import com.google.android.material.internal.ContextUtils.getActivity
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
    lateinit var usuarioEscolhido : UsuarioDTO
    lateinit var usuarioLogadoId : String
    lateinit var recycler : RecyclerView
    val adapter = GroupieAdapter()
    val usuarioAtual = UltimasMensagensActivity.usuarioAtual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        recycler = findViewById(R.id.recycler_chat)
        usuarioLogadoId = FirebaseAuth.getInstance().uid.toString()
        usuarioEscolhido = intent.getParcelableExtra(NovaMensagemActivity.USER_KEY)!!
        if (usuarioEscolhido != null) {
            supportActionBar?.title = usuarioEscolhido!!.username
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listenerNovasMensagens()

        val botao_enviar = findViewById<ImageButton>(R.id.enviar_mensagem)
        botao_enviar.setOnClickListener {
            enviaMensagem()
        }
    }

    private fun listenerNovasMensagens(){
        val ref = FirebaseDatabase.getInstance().getReference("/mensagens-usuarios/$usuarioLogadoId/${usuarioEscolhido.uid}")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val mensagemChat = snapshot.getValue(ChatDTO::class.java)
                if (mensagemChat != null) {
                    if(mensagemChat.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(ItemChatDireita(mensagemChat.text, usuarioAtual))
                    }else{
                    adapter.add(ItemChatEsquerda(mensagemChat.text, usuarioEscolhido.fotoPerfil))
                    }
                }
                recycler.adapter = adapter
                recycler.scrollToPosition(adapter.itemCount-1)
            }

            //boilerplate
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun enviaMensagem(){
        campoMensagem = findViewById(R.id.campo_mensagem)
        var mensagem = campoMensagem.text.toString().trim()
        if(mensagem.replace("\\s/g".toRegex(), "").length == 0){
            return
        }
        if(usuarioLogadoId == null && usuarioEscolhido.uid == null ) {
            toast("Não foi possível enviar essa mensagem. \n Tente novamente mais tarde.")
            return
        }
        val database = FirebaseDatabase.getInstance().getReference("/mensagens-usuarios/$usuarioLogadoId/${usuarioEscolhido.uid}").push()
        val toDatabase = FirebaseDatabase.getInstance().getReference("/mensagens-usuarios/${usuarioEscolhido.uid}/$usuarioLogadoId").push()
        val chatDto = ChatDTO(database.key, mensagem, usuarioLogadoId, usuarioEscolhido.uid, System.currentTimeMillis() / 1000)
        database.setValue(chatDto)
            .addOnSuccessListener {
                toDatabase.setValue(chatDto)
                campoMensagem.text.clear()
                recycler.scrollToPosition(adapter.itemCount-1)
            }
        val ultimaMensagemDatabase = FirebaseDatabase.getInstance()
            .getReference("/ultima-mensagem/$usuarioLogadoId/${usuarioEscolhido.uid}")
        ultimaMensagemDatabase.setValue(chatDto)
        val ultimaMensagemToDatabase = FirebaseDatabase.getInstance()
            .getReference("/ultima-mensagem/${usuarioEscolhido.uid}/$usuarioLogadoId")
        ultimaMensagemToDatabase.setValue(chatDto)
        //ItemUltimaMensagem.botaoNotificacaoFlag = false
    }
}

class ItemChatEsquerda(val text : String, val url : String) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.linha_chat_esquerda
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.campo_linha_esquerda).text = text
        Picasso.get().load(url)
            .into(viewHolder.itemView.findViewById<ImageView>(R.id.foto_chat_esquerda))
    }

}

class ItemChatDireita(val text : String, val usuario : UsuarioDTO) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.linha_chat_direita
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.campo_linha_direita).text = text
        val url = usuario.fotoPerfil
        Picasso.get().load(url)
            .into(viewHolder.itemView.findViewById<ImageView>(R.id.foto_chat_direita))
    }

}