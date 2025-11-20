package com.example.projeto_suporte.ui

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.R

class ChatsAgenteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chats_agente)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        val recycler = findViewById<RecyclerView>(R.id.recyclerChatsAgente)
        recycler.layoutManager = LinearLayoutManager(this)

        val listaChats = listOf(
            ChatAgente("Maria", "Chamado resolvido", R.drawable.ic_email, "verde"),
            ChatAgente("João", "Aguardando resposta", R.drawable.ic_email, "amarelo"),
            ChatAgente("Paula", "Offline", R.drawable.ic_email, "cinza"),
            ChatAgente("Rafael", "Novo chamado", R.drawable.ic_email, "verde")
        )

        val adapter = ChatAgenteAdapter(listaChats) { chat ->
            // Clique em um chat
        }

        recycler.adapter = adapter
    }
}
