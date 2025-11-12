package com.example.projeto_suporte.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.R

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val nomePessoa = intent.getStringExtra("nome")
        title = nomePessoa ?: "Chat"
    }
}
