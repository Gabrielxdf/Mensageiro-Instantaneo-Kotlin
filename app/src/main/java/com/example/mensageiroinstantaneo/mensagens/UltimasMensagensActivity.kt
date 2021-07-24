package com.example.mensageiroinstantaneo.mensagens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
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
    val adapter = GroupieAdapter()
    val ultimasMensagensHash = HashMap<String, ChatDTO>()
    companion object{
        lateinit var usuarioAtual : UsuarioDTO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ultimas_mensagens)
        supportActionBar?.title = "Mensageiro"
        supportActionBar?.setIcon(R.drawable.chat);
        recycler = findViewById(R.id.recycler_ultimas_mensagens)
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val itemUsuario = item as ItemUltimaMensagem
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(NovaMensagemActivity.USER_KEY, itemUsuario.parceiroChat)
            //ItemUltimaMensagem.botaoNotificacaoFlag = true
            startActivity(intent)
        }
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

    private fun atualizaRecyclerView(){
        adapter.clear()

        ultimasMensagensHash.keys.forEach{
            adapter.add(ItemUltimaMensagem(ultimasMensagensHash.getValue(it), it, null))
        }
    }
}

class ItemUltimaMensagem(val mensagem : ChatDTO, val chatPartner : String, var parceiroChat : UsuarioDTO?) : Item<GroupieViewHolder>(){
    //companion object {
        //var botaoNotificacaoFlag = false
    //}
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        if (mensagem.fromId == chatPartner && !botaoNotificacaoFlag){
//            viewHolder.itemView.findViewById<Button>(R.id.botao_notificacao).visibility = View.VISIBLE
//        }
        if (mensagem.fromId == chatPartner){
            viewHolder.itemView.findViewById<Button>(R.id.botao_notificacao).visibility = View.VISIBLE
        }
        var userPartner : UsuarioDTO?
        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$chatPartner")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userPartner = snapshot.getValue(UsuarioDTO::class.java)!!
                parceiroChat = userPartner
                viewHolder.itemView.findViewById<TextView>(R.id.mensagem_usuario_ultima_mensagem).text = mensagem.text
                viewHolder.itemView.findViewById<TextView>(R.id.nome_usuario_ultima_mensagem).text = userPartner?.username
                Picasso.get().load(userPartner?.fotoPerfil).into(viewHolder.itemView
                    .findViewById<ImageView>(R.id.foto_usuario_ultima_mensagem))
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun getLayout(): Int {
        return R.layout.linha_ultima_mensagem
    }

}