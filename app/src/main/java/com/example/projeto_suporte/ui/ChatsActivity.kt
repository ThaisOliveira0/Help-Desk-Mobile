package com.example.projeto_suporte.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.R
import com.example.projeto_suporte.databinding.ActivityChatsBinding

class ChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listaChats = listOf(
            Chat("João - Suporte", "Seu ticket foi atualizado", R.drawable.ic_suporte),
            Chat("Maria - Suporte", "Estou analisando seu problema...", R.drawable.ic_suporte),
            Chat("Equipe Técnica", "Sua solicitação está em andamento", R.drawable.ic_suporte)
        )

        binding.recyclerChats.layoutManager = LinearLayoutManager(this)

        binding.recyclerChats.adapter = ChatAdapter(listaChats) { chat ->
        }
        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }
}