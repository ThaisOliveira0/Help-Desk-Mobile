package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityResponderChamadoBinding
import com.example.projeto_suporte.model.Chamado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ResponderChamadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResponderChamadoBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResponderChamadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val chamadoId = intent.getStringExtra("chamado_id")
        val userId = intent.getStringExtra("user_id")

        if (chamadoId == null || chamadoId.isEmpty()) {
            Toast.makeText(this, "Erro Crítico: ID do CHAMADO não recebido.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Erro Crítico: ID do USUÁRIO não recebido.", Toast.LENGTH_LONG).show()
            binding.tvNomeUsuario.text = "Usuário: (ID não veio na Intent)"
        } else {
            carregarNomeDoUsuario(userId)
        }

        carregarDadosDoChamado(chamadoId)
        setupListeners(chamadoId)
    }


    private fun carregarDadosDoChamado(id: String) {
        db.collection("Chamados").document(id).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val chamado = document.toObject(Chamado::class.java)
                    if (chamado != null) {
                        binding.tvIdChamado.text = "Chamado: ${chamado.numeroChamado}"
                        binding.tvCategoria.text = "Categoria: ${chamado.categoria}"
                        binding.tvDescricaoChamado.text = chamado.descricao
                    }
                }
            }
    }

    private fun carregarNomeDoUsuario(userId: String) {
        Log.d("DEBUG_FIRESTORE", "ResponderChamado: Buscando usuário onde o CAMPO 'id' é: $userId")
        binding.tvNomeUsuario.text = "Usuário: Carregando..."

        if (userId.isNotEmpty()) {

            db.collection("Usuarios")
                .whereEqualTo("id", userId)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents != null && !documents.isEmpty) {
                        val document = documents.documents[0]
                        val nome = document.getString("nome") ?: "Usuário (sem nome)"
                        binding.tvNomeUsuario.text = "Usuário: $nome"
                        Log.d("DEBUG_FIRESTORE", "ResponderChamado: Nome encontrado via query: $nome")
                    } else {
                        binding.tvNomeUsuario.text = "Usuário: (Não encontrado)"
                        Log.w("DEBUG_FIRESTORE", "ResponderChamado: Nenhum usuário encontrado com o campo id: $userId")
                    }
                }
                .addOnFailureListener { exception ->
                    binding.tvNomeUsuario.text = "Usuário: (Erro na busca)"
                    Log.e("DEBUG_FIRESTORE", "ResponderChamado: Falha ao fazer query por usuário com id: $userId", exception)
                }
        } else {
            binding.tvNomeUsuario.text = "Usuário: (ID de usuário vazio)"
        }
    }

    private fun setupListeners(chamadoId: String) {
        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnEnviarResposta.setOnClickListener {
            val resposta = binding.edtResposta.text.toString().trim()
            val atendenteId = auth.currentUser?.uid
            if (resposta.isNotBlank() && atendenteId != null) {
                enviarRespostaEFecharChamado(chamadoId, resposta, atendenteId)
            } else {
                Toast.makeText(this, "Digite uma resposta.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enviarRespostaEFecharChamado(id: String, resposta: String, atendenteId: String) {
        val atualizacoes = mapOf(
            "status" to "Fechado",
            "resposta" to resposta,
            "idAtendente" to atendenteId
        )
        db.collection("Chamados").document(id).update(atualizacoes)
            .addOnSuccessListener {
                Toast.makeText(this, "Resposta enviada e chamado fechado!", Toast.LENGTH_LONG).show()

                val intent = Intent(this, MenuAgenteActivity::class.java)

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

                startActivity(intent)

                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Falha ao enviar resposta.", Toast.LENGTH_SHORT).show()
            }
    }
}
