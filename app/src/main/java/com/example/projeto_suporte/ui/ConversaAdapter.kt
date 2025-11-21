package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.R
import com.example.projeto_suporte.databinding.ItemConversaBinding
import com.example.projeto_suporte.model.Chat // CRIE ESTE MODELO (passo seguinte)

class ConversaAdapter(
    private var conversas: List<Chat>,
    private val onItemClick: (Chat) -> Unit
) : RecyclerView.Adapter<ConversaAdapter.ConversaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversaViewHolder {
        val binding = ItemConversaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ConversaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversaViewHolder, position: Int) {
        val conversa = conversas[position]
        holder.bind(conversa)
    }

    override fun getItemCount(): Int = conversas.size

    fun updateData(novaLista: List<Chat>) {
        this.conversas = novaLista
        notifyDataSetChanged()
    }

    inner class ConversaViewHolder(private val binding: ItemConversaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            binding.txtNomeConversa.text = "Chat com ${chat.clienteNome}"
            binding.txtUltimaMensagem.text = chat.ultimaMensagem

            // Define a cor do ponto de status
            val statusColor = when (chat.statusChat) {
                "solicitado" -> R.color.status_em_analise // Amarelo para solicitado
                "ativo" -> R.color.status_fechado      // Verde para ativo
                "encerrado" -> R.color.status_aberto    // Cinza para encerrado
                else -> android.R.color.transparent
            }
            binding.imgStatusChat.setColorFilter(
                ContextCompat.getColor(binding.root.context, statusColor)
            )

            binding.root.setOnClickListener {
                onItemClick(chat)
            }
        }
    }
}
