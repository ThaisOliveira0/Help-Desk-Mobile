package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityCriarChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class CriarChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCriarChatBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriarChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupToolbar()

        binding.btnEnviarSolicitacaoChat.setOnClickListener {
            enviarSolicitacao()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarCriarChat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarCriarChat.setNavigationOnClickListener {
            finish()
        }
    }

    private fun enviarSolicitacao() {
        val cliente = auth.currentUser
        val assunto = binding.edtAssuntoChat.text.toString().trim()
        val descricao = binding.edtDescricaoProblema.text.toString().trim()

        if (cliente == null) {
            Toast.makeText(this, "Erro de autenticação.", Toast.LENGTH_SHORT).show()
            return
        }

        if (assunto.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnEnviarSolicitacaoChat.isEnabled = false

        // Gera um ID único para este novo chat
        val novoChatId = UUID.randomUUID().toString()
        val clienteNome = cliente.displayName ?: "Cliente"

        // Combina assunto e descrição para a primeira mensagem
        val primeiraMensagem = "Assunto: $assunto\n\n$descricao"

        val novaSolicitacao = hashMapOf(
            "clienteId" to cliente.uid,
            "agenteId" to "",
            "clienteNome" to clienteNome,
            "statusChat" to "solicitado",
            "ultimaMensagem" to assunto, // Mostra o assunto na lista do agente
            "timestampUltimaMensagem" to System.currentTimeMillis()
        )

        // Cria o documento do chat e, em seguida, a primeira mensagem dentro dele
        db.collection("Chats").document(novoChatId).set(novaSolicitacao)
            .addOnSuccessListener {
                adicionarPrimeiraMensagem(novoChatId, primeiraMensagem, cliente.uid)
            }
            .addOnFailureListener { e ->
                Log.e("CRIAR_CHAT", "Falha ao criar solicitação", e)
                binding.btnEnviarSolicitacaoChat.isEnabled = true
            }
    }

    private fun adicionarPrimeiraMensagem(chatId: String, texto: String, remetenteId: String) {
        val mensagem = hashMapOf(
            "texto" to texto,
            "remetenteId" to remetenteId,
            "timestamp" to com.google.firebase.firestore.FieldValue.serverTimestamp()
        )

        db.collection("Chats").document(chatId).collection("Mensagens").add(mensagem)
            .addOnSuccessListener {
                Toast.makeText(this, "Solicitação enviada!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("chat_id", chatId)
                startActivity(intent)
                finish() // Fecha a tela de criação
            }
            .addOnFailureListener { e ->
                Log.e("CRIAR_CHAT", "Falha ao adicionar primeira mensagem", e)
                binding.btnEnviarSolicitacaoChat.isEnabled = true
            }
    }
}
