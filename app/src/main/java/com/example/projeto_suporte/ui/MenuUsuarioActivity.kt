package com.example.projeto_suporte.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.databinding.ActivityMenuUsuarioBinding
import com.example.projeto_suporte.model.Chamado
import com.example.projeto_suporte.ui.TicketAdapter
import com.example.projeto_suporte.ui.AbrirTicketActivity
import com.example.projeto_suporte.ui.DetalheTicketActivity
import com.example.projeto_suporte.ui.ChatsActivity
import com.example.projeto_suporte.ui.models.Ticket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MenuUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuUsuarioBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var ticketAdapter: TicketAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val usuario = FirebaseAuth.getInstance().currentUser
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        if (usuario != null) {
            binding.tvNomeUsuario.text = "Olá, ${usuario.displayName}"
        } else {
            finish()
            return
        }

        setupRecyclerView()
        carregarChamadosDoUsuario()

        binding.btnChats.setOnClickListener {
            val intent = Intent(this, ListaChatsActivity::class.java)
            startActivity(intent)
        }

        binding.btnNovoTicket.setOnClickListener {
            startActivity(Intent(this, AbrirTicketActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.fabNovoChat.setOnClickListener {
            val intent = Intent(this, CriarChatActivity::class.java) // Navega para a nova tela
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //chama os dados dnv ao recarregar a tela
        carregarChamadosDoUsuario()
    }

    private fun setupRecyclerView() {
        binding.recyclerTickets.layoutManager = LinearLayoutManager(this)
        // Inicializa o adapter com uma lista vazia
        ticketAdapter = TicketAdapter(emptyList()) { chamado ->
            val intent = Intent(this, DetalheTicketActivity::class.java)
            intent.putExtra("ticket_id", chamado.id) // Passa o ID único do Firestore
            startActivity(intent)
        }
        binding.recyclerTickets.adapter = ticketAdapter
    }

    private fun carregarChamadosDoUsuario() {
        val usuario = auth.currentUser
        if (usuario != null) {
            db.collection("Chamados")
                .whereEqualTo("userId", usuario.uid) // Filtra para pegar apenas os chamados do usuário logado
                .orderBy("numeroChamado", Query.Direction.DESCENDING) // Ordena pelos mais recentes
                .get()
                .addOnSuccessListener { documents ->
                    if (documents != null) {
                        val listaChamados = documents.toObjects(Chamado::class.java)
                        // Atualiza a lista no adapter
                        ticketAdapter.updateData(listaChamados)
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao buscar chamados. Verifique o Logcat.", Toast.LENGTH_LONG).show()
                    Log.e("FirestoreError", "Erro ao buscar chamados: ", exception)
                }
        }
    }
}
