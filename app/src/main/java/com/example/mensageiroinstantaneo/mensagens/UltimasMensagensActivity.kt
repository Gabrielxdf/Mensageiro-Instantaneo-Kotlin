package com.example.mensageiroinstantaneo.mensagens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mensageiroinstantaneo.NovaMensagemActivity
import com.example.mensageiroinstantaneo.R
import com.example.mensageiroinstantaneo.RegistroActivity
import com.example.mensageiroinstantaneo.modelo.ChatDTO
import com.example.mensageiroinstantaneo.modelo.UsuarioDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class UltimasMensagensActivity : AppCompatActivity() {
    lateinit var recycler : RecyclerView
    //lateinit var botao_notificacao : Button
    val adapter = GroupieAdapter()

    companion object{
        lateinit var usuarioAtual : UsuarioDTO
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ultimas_mensagens)
        recycler = findViewById(R.id.recycler_ultimas_mensagens)
        //botao_notificacao = findViewById(R.id.botao_notificacao)
        verificaUsuarioLogado()
        buscaUsuarioAtual()
        atualizaUltimasMensagens()

    }
    private fun buscaUsuarioAtual(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                usuarioAtual = snapshot.getValue(UsuarioDTO::class.java)!!
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun verificaUsuarioLogado(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this, RegistroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_nova_mensagem -> {
                startActivity(Intent(this, NovaMensagemActivity::class.java))
            }
            R.id.menu_sair -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegistroActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    val ultimasMensagensHash = HashMap<String, ChatDTO>()
    private fun atualizaRecyclerView(){
        adapter.clear()
        ultimasMensagensHash.values.forEach{
            adapter.add(ItemUltimaMensagem(it))
        }
    }
    private fun atualizaUltimasMensagens(){
        val uid = FirebaseAuth.getInstance().uid.toString()
        val database = FirebaseDatabase.getInstance().getReference("/ultima-mensagem/$uid")
        database.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val mensagem = snapshot.getValue(ChatDTO::class.java) ?: return
                    ultimasMensagensHash[snapshot.key!!] = mensagem
                atualizaRecyclerView()
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val mensagem = snapshot.getValue(ChatDTO::class.java) ?: return
                ultimasMensagensHash[snapshot.key!!] = mensagem
                atualizaRecyclerView()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
        recycler.adapter = adapter
    }
}

class ItemUltimaMensagem(val mensagem : ChatDTO) : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.mensagem_usuario_ultima_mensagem).text = mensagem.text
        //viewHolder.itemView.findViewById<TextView>(R.id.nome_usuario_nova_mensagem).text = usuario.username
        //Picasso.get().load(usuario.fotoPerfil).into(viewHolder.itemView
            //.findViewById<ImageView>(R.id.imagem_usuario_nova_mensagem))
    }

    override fun getLayout(): Int {
        return R.layout.linha_ultima_mensagem
    }

}