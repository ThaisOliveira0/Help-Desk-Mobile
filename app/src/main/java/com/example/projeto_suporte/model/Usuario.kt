package com.example.projeto_suporte.model

import com.example.projeto_suporte.enums.TipoUsuario

data class Usuario (
    val id: String,
    val nome: String,
    val sobrenome: String,
    val email: String,
    val dataNasc: String,
    val tipoUsuario: TipoUsuario
)