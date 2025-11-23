package com.example.projeto_suporte.model

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Chamado(
    var id: String = "",
    val userId: String = "",
    val numeroChamado: String = "",
    val categoria: String = "",
    val descricao: String = "",
    val dataAbertura: String = "",
    val status: String = "Aberto",
    val resposta: String = "",
    val idAgente: String = ""
) : Parcelable
