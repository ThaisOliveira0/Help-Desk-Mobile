package com.example.projeto_suporte.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.R

data class ChatAgente(
    val nome: String,
    val ultimaMsg: String,
    val foto: Int,
    val status: String
)

class ChatAgenteAdapter(
    private val lista: List<ChatAgente>,
    private val onItemClick: (ChatAgente) -> Unit
) : RecyclerView.Adapter<ChatAgenteAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.textName)
        val ultimaMsg: TextView = itemView.findViewById(R.id.textLastMessage)
        val foto: ImageView = itemView.findViewById(R.id.imageUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_agente, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = lista[position]

        holder.nome.text = chat.nome
        holder.ultimaMsg.text = chat.ultimaMsg
        holder.foto.setImageResource(chat.foto)

        val cor = when (chat.status.lowercase()) {
            "verde" -> "#2ECC71"
            "amarelo" -> "#F1C40F"
            "cinza" -> "#95A5A6"
            else -> "#D0D0D0"
        }

        holder.itemView.setOnClickListener {
            onItemClick(chat)
        }
    }

    override fun getItemCount() = lista.size
}
