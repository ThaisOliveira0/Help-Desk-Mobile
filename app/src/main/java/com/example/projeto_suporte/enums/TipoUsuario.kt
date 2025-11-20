package com.example.projeto_suporte.enums;

enum class TipoUsuario (val tipo: String) {
    CLIENTE("cliente"),
    AGENTE("agente"),
    ADMIN("admin");

    companion object {
        fun fromString(tipo: String): TipoUsuario {
            // Procura no enum um valor cujo 'tipo' corresponda à string recebida
            return entries.find { it.tipo.equals(tipo, ignoreCase = true) }
                ?: CLIENTE // Se não encontrar, retorna CLIENTE como padrão seguro
        }
    }
}
