package com.example.mensageiroinstantaneo.mensagens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.mensageiroinstantaneo.NovaMensagemActivity
import com.example.mensageiroinstantaneo.R
import com.example.mensageiroinstantaneo.RegistroActivity
import com.example.mensageiroinstantaneo.modelo.UsuarioDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UltimasMensagensActivity : AppCompatActivity() {
    companion object{
        lateinit var usuarioAtual : UsuarioDTO
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ultimas_mensagens)
        verificaUsuarioLogado()
        buscaUsuarioAtual()

    }
    private fun buscaUsuarioAtual(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                usuarioAtual = snapshot.getValue(UsuarioDTO::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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
}