package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.databinding.ActivityListaChatsBinding
import com.example.projeto_suporte.enums.TipoUsuario
import com.example.projeto_suporte.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ListaChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaChatsBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var conversaAdapter: ConversaAdapter
    private var isAgente: Boolean = false // Guarda se o usuário é um agente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupToolbar()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        carregarConversas()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarListaChats)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarListaChats.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        conversaAdapter = ConversaAdapter(emptyList()) { chat ->
            handleChatClick(chat)
        }
        binding.recyclerListaChats.layoutManager = LinearLayoutManager(this)
        binding.recyclerListaChats.adapter = conversaAdapter
    }

    private fun handleChatClick(chat: Chat) {
        // Para as queries de chat, usamos o UID, que está nos campos agenteId e clienteId
        val usuarioAtualUid = auth.currentUser?.uid ?: return

        if (isAgente && chat.statusChat == "solicitado") {
            aceitarChat(chat, usuarioAtualUid)
        } else {
            navegarParaChat(chat.id)
        }
    }

    private fun aceitarChat(chat: Chat, agenteId: String) {
        val chatRef = db.collection("Chats").document(chat.id)

        chatRef.update(
            mapOf(
                "agenteId" to agenteId,
                "statusChat" to "ativo"
            )
        )
            .addOnSuccessListener {
                Log.d("ACEITAR_CHAT", "Agente $agenteId aceitou o chat ${chat.id}")
                Toast.makeText(this, "Você aceitou o chat.", Toast.LENGTH_SHORT).show()
                navegarParaChat(chat.id)
            }
            .addOnFailureListener { e ->
                Log.e("ACEITAR_CHAT", "Falha ao aceitar o chat", e)
                Toast.makeText(this, "Não foi possível aceitar o chat. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navegarParaChat(chatId: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chat_id", chatId)
        startActivity(intent)
    }

    private fun carregarConversas() {
        val usuarioAtual = auth.currentUser
        if (usuarioAtual == null) {
            Log.e("ListaChats", "Usuário não logado!")
            binding.txtSemChats.text = "Erro: Usuário não autenticado."
            binding.txtSemChats.visibility = View.VISIBLE
            binding.recyclerListaChats.visibility = View.GONE
            return
        }

        // Pega o EMAIL do usuário logado para usar como ID do documento na coleção "Usuarios"
        val idDocumentoUsuario = usuarioAtual.email
        if (idDocumentoUsuario.isNullOrEmpty()) {
            Log.e("ListaChats", "Email do usuário é nulo ou vazio. Não é possível buscar o perfil.")
            binding.txtSemChats.text = "Erro: Email do usuário não encontrado."
            binding.txtSemChats.visibility = View.VISIBLE
            return
        }

        Log.d("ListaChats_DIAG", "Buscando perfil no Firestore com o ID (email): $idDocumentoUsuario")

        // Busca o documento na coleção "Usuarios" usando o EMAIL como ID
        db.collection("Usuarios").document(idDocumentoUsuario).get().addOnSuccessListener { userDoc ->

            if (!userDoc.exists()) {
                Log.e("ListaChats_ERRO", "Documento para o usuário $idDocumentoUsuario NÃO EXISTE no Firestore!")
                binding.txtSemChats.text = "Perfil de usuário não encontrado no banco de dados."
                binding.txtSemChats.visibility = View.VISIBLE
                return@addOnSuccessListener
            }

            val tipoUsuarioString = userDoc.getString("tipoUsuario") ?: "cliente"
            val tipoUsuarioEnum = TipoUsuario.fromString(tipoUsuarioString)
            this.isAgente = tipoUsuarioEnum == TipoUsuario.AGENTE

            Log.d("ListaChats_DIAG", "Usuário é agente? $isAgente")

            val query: Query
            // Para as queries de chat, usamos o UID, que está nos campos 'clienteId' e 'agenteId'
            val usuarioAtualUid = usuarioAtual.uid

            if (isAgente) {
                Log.d("ListaChats", "Carregando conversas para AGENTE.")
                query = db.collection("Chats").where(
                    com.google.firebase.firestore.Filter.or(
                        com.google.firebase.firestore.Filter.equalTo("agenteId", usuarioAtualUid),
                        com.google.firebase.firestore.Filter.equalTo("agenteId", "")
                    )
                )
            } else {
                Log.d("ListaChats", "Carregando conversas para CLIENTE.")
                query = db.collection("Chats").whereEqualTo("clienteId", usuarioAtualUid)
            }

            query.orderBy("timestampUltimaMensagem", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        if (error.message?.contains("requires an index") == true) {
                            Log.e("ListaChats", "ERRO DE ÍNDICE DO FIRESTORE. Verifique o Logcat para o link de criação do índice.", error)
                            binding.txtSemChats.text = "Erro de configuração do banco de dados (Índice)."
                        } else {
                            Log.e("ListaChats", "Erro ao carregar conversas", error)
                            binding.txtSemChats.text = "Erro ao carregar conversas."
                        }
                        binding.txtSemChats.visibility = View.VISIBLE
                        return@addSnapshotListener
                    }

                    if (snapshots != null && !snapshots.isEmpty) {
                        val listaConversas = snapshots.map { doc ->
                            val chat = doc.toObject(Chat::class.java)
                            chat.id = doc.id
                            chat
                        }

                        if (listaConversas.isEmpty()) {
                            binding.recyclerListaChats.visibility = View.GONE
                            binding.txtSemChats.text = "Nenhuma conversa encontrada."
                            binding.txtSemChats.visibility = View.VISIBLE
                        } else {
                            binding.recyclerListaChats.visibility = View.VISIBLE
                            binding.txtSemChats.visibility = View.GONE
                            conversaAdapter.updateData(listaConversas)
                        }

                    } else {
                        binding.recyclerListaChats.visibility = View.GONE
                        binding.txtSemChats.text = "Nenhuma conversa encontrada."
                        binding.txtSemChats.visibility = View.VISIBLE
                        conversaAdapter.updateData(emptyList())
                    }
                }
        }.addOnFailureListener {
            Log.e("ListaChats", "Não foi possível verificar o tipo do usuário.", it)
            binding.txtSemChats.text = "Não foi possível verificar seu perfil."
            binding.txtSemChats.visibility = View.VISIBLE
        }
    }
}
