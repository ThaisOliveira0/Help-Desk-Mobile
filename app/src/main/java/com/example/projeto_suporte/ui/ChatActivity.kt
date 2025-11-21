package com.example.projeto_suporte.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.databinding.ActivityChatBinding
import com.example.projeto_suporte.model.Mensagem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var mensagemAdapter: MensagemAdapter
    private var chatId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        chatId = intent.getStringExtra("chat_id")
        if (chatId.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: ID do chat não encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setupToolbar()
        setupRecyclerView()
        setupListeners()

        carregarMensagens()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarChat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarChat.setNavigationOnClickListener {
            finish()
        }
        // Opcional: Carregar nome do cliente no título
        db.collection("Chats").document(chatId!!).get().addOnSuccessListener {
            val nomeCliente = it.getString("clienteNome") ?: "Chat"
            supportActionBar?.title = "Chat com $nomeCliente"
        }
    }

    private fun setupRecyclerView() {
        mensagemAdapter = MensagemAdapter(emptyList())
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true // Faz a lista começar de baixo
        binding.recyclerMensagens.layoutManager = layoutManager
        binding.recyclerMensagens.adapter = mensagemAdapter
    }

    private fun setupListeners() {
        binding.btnEnviar.setOnClickListener {
            val textoMensagem = binding.edtMensagem.text.toString().trim()
            if (textoMensagem.isNotEmpty()) {
                enviarMensagem(textoMensagem)
            }
        }
    }

    private fun carregarMensagens() {
        chatId?.let { id ->
            db.collection("Chats").document(id).collection("Mensagens")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        Log.e("ChatActivity", "Erro ao carregar mensagens.", error)
                        return@addSnapshotListener
                    }

                    snapshots?.let {
                        val listaMensagens = it.toObjects(Mensagem::class.java)
                        mensagemAdapter.updateData(listaMensagens)
                        // Rola para a última mensagem
                        binding.recyclerMensagens.scrollToPosition(listaMensagens.size - 1)
                    }
                }
        }
    }

    private fun enviarMensagem(texto: String) {
        val remetenteId = auth.currentUser?.uid
        if (remetenteId.isNullOrEmpty() || chatId.isNullOrEmpty()) {
            Toast.makeText(this, "Erro ao enviar: usuário ou chat inválido.", Toast.LENGTH_SHORT).show()
            return
        }

        val mensagem = Mensagem(
            texto = texto,
            remetenteId = remetenteId,
            timestamp = Date() // Será sobrescrito pelo @ServerTimestamp
        )

        // Adiciona a mensagem na subcoleção
        db.collection("Chats").document(chatId!!)
            .collection("Mensagens").add(mensagem)
            .addOnSuccessListener {
                binding.edtMensagem.text.clear() // Limpa o campo de texto
                // Atualiza a "última mensagem" no documento principal do chat
                atualizarMetadadosChat(texto)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Falha ao enviar mensagem.", Toast.LENGTH_SHORT).show()
                Log.e("ChatActivity", "Erro ao enviar mensagem", it)
            }
    }

    private fun atualizarMetadadosChat(ultimaMsg: String) {
        chatId?.let {
            val atualizacoes = mapOf(
                "ultimaMensagem" to ultimaMsg,
                "timestampUltimaMensagem" to System.currentTimeMillis()
            )
            db.collection("Chats").document(it).update(atualizacoes)
        }
    }
}
