package com.example.projeto_suporte.ui

sealed class ItemHistorico {

    data class CategoriaHeader(val categoria: String) : ItemHistorico()

    data class ChamadoItem(
        val id: Int,
        val data: String,
        val categoria: String
    ) : ItemHistorico()
}
