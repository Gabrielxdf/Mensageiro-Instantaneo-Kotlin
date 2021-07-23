package com.example.mensageiroinstantaneo.modelo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UsuarioDTO(val uid: String, val username : String, val fotoPerfil : String) : Parcelable{
    constructor() : this("","","")
}