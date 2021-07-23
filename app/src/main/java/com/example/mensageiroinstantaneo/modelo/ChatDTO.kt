package com.example.mensageiroinstantaneo.modelo

class ChatDTO(val id: String?, val text: String, val fromId: String, val toId: String, val timeStamp: Long) {
    constructor() : this("", "","","",-1)
}