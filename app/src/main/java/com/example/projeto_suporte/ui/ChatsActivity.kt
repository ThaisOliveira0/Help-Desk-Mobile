package com.example.projeto_suporte.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.R
import com.example.projeto_suporte.databinding.ActivityCadastroBinding
import com.example.projeto_suporte.databinding.ActivityChatsBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val nomePessoa = intent.getStringExtra("nome")
        title = nomePessoa ?: "Chat"
    }
}
