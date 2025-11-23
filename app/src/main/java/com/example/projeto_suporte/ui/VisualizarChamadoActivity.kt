package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityVisualizarChamadoBinding
import com.example.projeto_suporte.model.Chamado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VisualizarChamadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisualizarChamadoBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualizarChamadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val chamadoId = intent.getStringExtra("chamado_id")
        val userId = intent.getStringExtra("user_id") //

        if (chamadoId == null || chamadoId.isEmpty()) {
            Toast.makeText(this, "Erro Crítico: ID do CHAMADO não recebido.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Erro Crítico: ID do USUÁRIO não recebido.", Toast.LENGTH_LONG).show()
            binding.txtNomeCliente.text = "Cliente: (ID de usuário não veio na Intent)"
        } else {
            carregarNomeDoCliente(userId)
        }

        carregarDetalhesDoChamado(chamadoId)
        setupListeners(chamadoId, userId)
    }

    private fun setupListeners(chamadoId: String, userId: String?) {
        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnMarcarAnalise.setOnClickListener {
            atualizarChamado(chamadoId, "Em Análise")
        }

        binding.btnContato.setOnClickListener {
            val intent = Intent(this, ResponderChamadoActivity::class.java)
            intent.putExtra("chamado_id", chamadoId)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }
    }

    private fun atualizarChamado(chamadoId: String, novoStatus: String) {
        // Pega o ID do agente atualmente logado
        val agenteId = auth.currentUser?.uid

        if (agenteId == null) {
            Toast.makeText(this, "Erro: Não foi possível identificar o agente.", Toast.LENGTH_SHORT).show()
            return
        }

        // Cria o mapa com os campos a serem atualizados: status e idAtendente
        val atualizacoes = mapOf(
            "status" to novoStatus,
            "idAgente" to agenteId
        )

        // Atualiza o documento no Firestore com o mapa
        db.collection("Chamados").document(chamadoId)
            .update(atualizacoes)
            .addOnSuccessListener {
                Toast.makeText(this, "Chamado atribuído a você e marcado como '$novoStatus'", Toast.LENGTH_LONG).show()
                binding.btnMarcarAnalise.visibility = View.GONE // Esconde o botão após a ação
                binding.btnContato.visibility = View.VISIBLE // Garante que o botão de contato apareça
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Falha ao atualizar o chamado", Toast.LENGTH_SHORT).show()
                Log.e("FIRESTORE_UPDATE", "Erro ao atualizar chamado", e)
            }
    }
    // highlight-end

    private fun preencherDadosNaTela(chamado: Chamado) {
        binding.txtNumeroChamado.text = "Chamado #${chamado.numeroChamado}"
        binding.txtCategoria.text = chamado.categoria
        binding.txtData.text = "Aberto em: ${chamado.dataAbertura}"
        binding.txtDescricao.text = chamado.descricao

        // Esconde os botões de ação se o chamado não estiver mais "Aberto"
        if (chamado.status != "Aberto") {
            binding.btnContato.visibility = View.GONE
            binding.btnMarcarAnalise.visibility = View.GONE
        }
    }

    private fun carregarNomeDoCliente(userId: String) {
        Log.d("DEBUG_FIRESTORE", "Buscando usuário onde o CAMPO 'id' é: $userId")
        binding.txtNomeCliente.text = "Cliente: Carregando..."

        if (userId.isNotEmpty()) {

            db.collection("Usuarios")
                .whereEqualTo("id", userId)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents != null && !documents.isEmpty) {
                        val document = documents.documents[0]
                        val nome = document.getString("nome") ?: "Cliente (sem nome)"
                        binding.txtNomeCliente.text = "Cliente: $nome"
                        Log.d("DEBUG_FIRESTORE", "Nome encontrado via query: $nome")
                    } else {
                        binding.txtNomeCliente.text = "Cliente: (Usuário não encontrado)"
                        Log.w("DEBUG_FIRESTORE", "Nenhum usuário encontrado com o campo id: $userId")
                    }
                }
                .addOnFailureListener { exception ->
                    binding.txtNomeCliente.text = "Cliente: (Erro na busca)"
                    Log.e("DEBUG_FIRESTORE", "Falha ao fazer query por usuário com id: $userId", exception)
                }
        } else {
            binding.txtNomeCliente.text = "Cliente: (ID de usuário vazio)"
        }
    }

    private fun carregarDetalhesDoChamado(id: String) {
        db.collection("Chamados").document(id).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val chamado = document.toObject(Chamado::class.java)
                    if (chamado != null) {
                        preencherDadosNaTela(chamado)
                    }
                }
            }
    }
}
