package com.example.projeto_suporte.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Chat(
    val clienteId: String = "",
    val agenteId: String = "",
    val clienteNome: String = "",
    val statusChat: String = "",
    val ultimaMensagem: String = "",
    val timestampUltimaMensagem: Long = 0L,

    // Adicionaremos o ID do documento aqui para facilitar o acesso
    var id: String = ""
)
