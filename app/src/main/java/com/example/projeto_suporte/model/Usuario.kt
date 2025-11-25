package com.example.projeto_suporte.model

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Usuario (
    var id: String = "",
    var nome: String = "",
    var sobrenome: String = "",
    var email: String = "",
    var dataNasc: String = "",
    var tipoUsuario: String = ""
) : Parcelable {
    override fun toString(): String {
        return "$nome $sobrenome"
    }
}