package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.databinding.ActivityMenuUsuarioBinding
import com.example.projeto_suporte.ui.TicketAdapter
import com.example.projeto_suporte.ui.AbrirTicketActivity
import com.example.projeto_suporte.ui.DetalheTicketActivity
import com.example.projeto_suporte.ui.ChatsActivity
import com.example.projeto_suporte.ui.models.Ticket

class MenuUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nomeUsuario = intent.getStringExtra("nome") ?: "Usuário"
        binding.tvNomeUsuario.text = "Olá, $nomeUsuario"

        binding.recyclerTickets.layoutManager = LinearLayoutManager(this)

        val listaTickets = listOf(
            Ticket("2548781651", "Problema com a impressora", "21/03/2024"),
            Ticket("4784512168", "Erro no sistema", "19/03/2024"),
            Ticket("1564879451", "Login não funciona", "18/03/2024")
        )

        binding.recyclerTickets.adapter = TicketAdapter(listaTickets) { ticket ->
            val intent = Intent(this, DetalheTicketActivity::class.java)
            intent.putExtra("ticket_id", ticket.id)
            startActivity(intent)
        }

        binding.btnChats.setOnClickListener {
            startActivity(Intent(this, ChatsActivity::class.java))
        }

        binding.btnNovoTicket.setOnClickListener {
            startActivity(Intent(this, AbrirTicketActivity::class.java))
        }
    }
}
