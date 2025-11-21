package com.example.projeto_suporte.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Mensagem(
    val texto: String = "",
    val remetenteId: String = "",
    @ServerTimestamp // O Firestore preenche isso automaticamente no servidor
    val timestamp: Date? = null
)
