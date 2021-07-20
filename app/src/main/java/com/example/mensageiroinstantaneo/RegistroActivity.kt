package com.example.mensageiroinstantaneo

import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class RegistroActivity : Utils() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botao = findViewById<Button>(R.id.botao_registro)
        val possuiConta = findViewById<TextView>(R.id.campo_ja_tem_conta)
        val botao_foto = findViewById<Button>(R.id.botao_foto_registro)

        botao.setOnClickListener {
            registra()
        }

        possuiConta.setOnClickListener {
            Log.d("RegistroActivity", "Teste do campo: já possui uma conta")
            //Inicia a LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //Para selecionar a foto do usuário
        val getImage = registerForActivityResult(ActivityResultContracts.GetContent())
        { uri: Uri? ->
            // Handle the returned Uri

            //Usa o ImageDecoder se a API level for 28+, se não, usa o método descontinuado.
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.createSource(contentResolver, uri!!)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
                TODO("VERSION.SDK_INT < P")
            }
            botao_foto.background = ImageDecoder.decodeDrawable(bitmap)
            TODO("Ajustar a imagem ao botão")

            Log.d("Main", "${uri.toString()}")
        }

        botao_foto.setOnClickListener {
            getImage.launch("image/*")

            //Método descontinuado
            //val intent = Intent(Intent.ACTION_PICK)
            //intent.type = "image/*"
            //startActivityForResult(intent, 0)
            //Depois sobreescrever a função onActivityResult

        }

    }

    private fun registra() {
        val campoUsuario = findViewById<EditText>(R.id.campo_usuario_registro)
        val campoEmail = findViewById<EditText>(R.id.campo_email_registro)
        val campoSenha = findViewById<EditText>(R.id.campo_senha_registro)

        val usuario = campoUsuario.text.toString().trim()
        val email = campoEmail.text.toString().trim()
        val senha = campoSenha.text.toString().trim()

        //Fazendo validações
        var flag = false
        if (usuario.isEmpty()) {
            campoUsuario.error = "Entre com um nome de usuário"
            campoUsuario.requestFocus()
            flag = true
        }
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

        //Criando usuário no Firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    try {
                        throw it.exception!!
                    } catch (e: FirebaseAuthException) {
                        Log.d("RegistroActivity", "Falha ao criar o usuário: ${traduz_erro(e)}")
                        toast(traduz_erro(e))
                    } finally {
                        return@addOnCompleteListener
                    }
                } else {
                    //else if isSuccessful
                    Log.d(
                        "RegistroActivity", "Criado o \n Usuário: $usuario \n " +
                                "E-mail: $email \n Senha: $senha \n uid: ${it.result?.user?.uid}"
                    )
                }
            }
    }
}