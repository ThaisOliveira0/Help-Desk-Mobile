package com.example.projeto_suporte.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chamado(
    val id: String = "",
    val userId: String = "",
    val numeroChamado: String = "",
    val categoria: String = "",
    val descricao: String = "",
    val dataAbertura: String = "",
    val status: String = "Aberto",
    val resposta: String = "",
    val idAtendente: String = ""
) : Parcelable
