package com.example.mensageiroinstantaneo.registrologin


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.mensageiroinstantaneo.R
import com.example.mensageiroinstantaneo.mensagens.UltimasMensagensActivity
import com.example.mensageiroinstantaneo.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginActivity : Utils() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val botao = findViewById<Button>(R.id.botao_login)
        val voltarRegistro = findViewById<TextView>(R.id.campo_voltar_registro)

        botao.setOnClickListener {
            login()
        }
        voltarRegistro.setOnClickListener {
            finish()
            Log.d("LoginActivity", "Finalizando a LoginActivity")
        }
    }

    private fun login() {
        val campoEmail = findViewById<EditText>(R.id.campo_email_login)
        val campoSenha = findViewById<EditText>(R.id.campo_senha_login)

        val email = campoEmail.text.toString().trim()
        val senha = campoSenha.text.toString().trim()

        //Fazendo validações
        var flag = false
        if (email.isEmpty()) {
            campoEmail.error = "Entre com um e-mail"
            campoEmail.requestFocus()
            flag = true
        }
        if (senha.isEmpty()) {
            campoSenha.error = "Entre com uma senha"
            campoSenha.requestFocus()
            flag = true
        }
        if(flag){
            return
        }

        //Fazendo o login do usuário no Firebase
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    try {
                        throw it.exception!!
                    } catch (e: FirebaseAuthException) {
                        toast("Desculpe, houve uma falha. \n ${traduz_erro(e)}")
                        Log.d("Login", "Falha ao fazer login do usuário: ${traduz_erro(e)}")
                    } finally {
                        return@addOnCompleteListener
                    }
                } else {
                    Log.d(
                        "LoginActivity", "Fez login com \n" +
                                "E-mail: $email \n Senha: $senha"
                    )
                    val intent = Intent(this, UltimasMensagensActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
    }
}