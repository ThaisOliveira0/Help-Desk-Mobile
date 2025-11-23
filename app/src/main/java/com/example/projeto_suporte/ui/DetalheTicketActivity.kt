package com.example.projeto_suporte.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityDetalheTicketBinding
import com.example.projeto_suporte.model.Chamado
import com.google.firebase.firestore.FirebaseFirestore

class DetalheTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalheTicketBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalheTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        val ticketId = intent.getStringExtra("ticket_id")

        if (ticketId == null || ticketId.isEmpty()) {
            Toast.makeText(this, "Erro: ID do chamado não encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        carregarDetalhesDoChamado(ticketId)

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun carregarDetalhesDoChamado(id: String) {
        db.collection("Chamados").document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val chamado = document.toObject(Chamado::class.java)
                    if (chamado != null) {
                        // 1. Preenche os dados do chamado na tela
                        preencherDados(chamado)
                        // 2. Inicia a busca pelo nome do atendente
                        carregarNomeDoAtendente(chamado.idAgente)
                    }
                } else {
                    Toast.makeText(this, "Chamado não encontrado.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao carregar detalhes: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun carregarNomeDoAtendente(agenteId: String) {
        // Se o idAgente estiver vazio, o chamado ainda não foi atribuído.
        if (agenteId.isEmpty()) {
            binding.tvAtendente.text = "Atendente: Aguardando atribuição"
            return
        }

        binding.tvAtendente.text = "Atendente: Carregando..."

        // Busca na coleção "Usuarios" pelo documento onde o CAMPO "id" é igual ao agenteId
        db.collection("Usuarios")
            .whereEqualTo("id", agenteId)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (documents != null && !documents.isEmpty) {
                    val agenteDocument = documents.documents[0]
                    val nomeAtendente = agenteDocument.getString("nome") ?: "Nome não disponível"
                    binding.tvAtendente.text = "Atendente: $nomeAtendente"
                } else {
                    // Caso a busca funcione mas não encontre o ID (pouco provável agora)
                    binding.tvAtendente.text = "Atendente: (Não localizado)"
                    Log.w("BuscaAtendente", "Nenhum documento de usuário encontrado com o campo 'id': $agenteId")
                }
            }
            .addOnFailureListener { exception ->
                // Caso a busca falhe por problemas de rede ou permissão
                binding.tvAtendente.text = "Atendente: (Erro ao buscar)"
                Log.e("BuscaAtendente", "Falha ao buscar atendente.", exception)
            }
    }

    @SuppressLint("SetTextI18n")
    private fun preencherDados(chamado: Chamado) {
        binding.tvIdTicket.text = "ID do Ticket: ${chamado.numeroChamado}"
        binding.tvCategoria.text = "Categoria: ${chamado.categoria}"
        binding.tvData.text = "Criado em: ${chamado.dataAbertura}"
        binding.tvDescricao.text = chamado.descricao
        // O status também é útil para o cliente
        binding.tvStatus.text = "Status: ${chamado.status}"
    }
    // ----> FIM DA LÓGICA CORRIGIDA <----
}
