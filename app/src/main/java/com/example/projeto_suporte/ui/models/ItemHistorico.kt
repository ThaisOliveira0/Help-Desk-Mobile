package com.example.projeto_suporte.ui.models

sealed class ItemHistorico {
    data class CategoriaHeader(val nome: String) : ItemHistorico()
    data class ChamadoItem(
        val id: String, // Usaremos o ID real do documento
        val data: String,
        val categoria: String,
        val numeroChamado: String // Adicionado para exibição
    ) : ItemHistorico()
}