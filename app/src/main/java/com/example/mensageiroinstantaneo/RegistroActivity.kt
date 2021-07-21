package com.example.mensageiroinstantaneo

import android.content.Intent
import android.graphics.ImageDecoder
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class RegistroActivity : Utils() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val botao = findViewById<Button>(R.id.botao_registro)
        val possuiConta = findViewById<TextView>(R.id.campo_ja_tem_conta)
        val botao_foto = findViewById<Button>(R.id.botao_foto_registro)

        botao.setOnClickListener {
            registra()
        }

        possuiConta.setOnClickListener {
            Log.d("Registro", "Teste do campo: já possui uma conta")
            //Inicia a LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //Para selecionar a foto do usuário
        val getImage = registerForActivityResult(ActivityResultContracts.GetContent())
        { uri: Uri? ->
            fotoUri = uri
            //Usa o ImageDecoder se a API level for 28+, se não, usa o método descontinuado.
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.createSource(contentResolver, fotoUri!!)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, fotoUri)
                TODO("VERSION.SDK_INT < P")
            }

            //Pondo a imagem no botão 3rd party para a imagem ficar redonda
            val botao_imageview = findViewById<CircleImageView>(R.id.botao_circle_imageview)
            botao_imageview.setImageBitmap(ImageDecoder.decodeBitmap(bitmap))
            botao_foto.alpha = 0f
            //botao_foto.background = ImageDecoder.decodeDrawable(bitmap)
        }

        botao_foto.setOnClickListener {
            getImage.launch("image/*")

        }

    }
    var fotoUri : Uri? = null

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
                        Log.d("Registro", "Falha ao criar o usuário: ${traduz_erro(e)}")
                        toast(traduz_erro(e))
                    } finally {
                        return@addOnCompleteListener
                    }
                } else {
                    toast("Registrado com sucesso!")
                    Log.d(
                        "Registro", "Criado o \n Usuário: $usuario \n " +
                                "E-mail: $email \n Senha: $senha \n uid: ${it.result?.user?.uid}"
                    )
                    salvarImagem(usuario)
                }
            }
    }

    private fun salvarImagem(usuario : String){
        if (fotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance().getReference("/imagens/$filename")
        storage.putFile(fotoUri!!)
            .addOnSuccessListener {
                Log.d("Registro", "Fez o upload da imagem com sucesso: ${it.metadata?.path}")
                storage.downloadUrl.addOnSuccessListener {
                    Log.d("Registro", "Localização do arquivo: $it")
                    salvarUsuario(usuario, it.toString())
                }
            }
            .addOnFailureListener{
                Log.d("Registro", "Falha ao salvar imagem: ${it.message}")
            }
    }

    private fun salvarUsuario(usuario : String, urlImagem : String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val database = FirebaseDatabase.getInstance().getReference("/usuarios/$uid")
        //val campoUsuario = findViewById<EditText>(R.id.campo_usuario_registro).text.toString().trim()
        val usuarioDto = UsuarioDTO(uid, usuario, urlImagem)
        //salvando os dados do usuário
        database.setValue(usuarioDto)
            .addOnSuccessListener {
                //o usuario pode digitar algo no campo de usuario dps
                Log.d("Registro", "O usuário foi salvo no Database do Firebase")
            }
            .addOnFailureListener {
                Log.d("Registro", "Falha ao salvar o usuário: ${it.message}")
            }
    }
}
class UsuarioDTO(val uid: String, val username : String, val fotoPerfil : String)