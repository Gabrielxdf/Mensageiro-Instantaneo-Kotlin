package com.example.mensageiroinstantaneo


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
        val email = findViewById<EditText>(R.id.campo_email_login).text.toString().trim()
        val senha = findViewById<EditText>(R.id.campo_senha_login).text.toString()

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
                }
            }
    }
}