package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.R

// Modelo de dados para cada chat
data class Chat(val nome: String, val ultimaMsg: String, val foto: Int)

class ChatAdapter(
    private val lista: List<Chat>,
    private val onItemClick: (Chat) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // Representa um item visual da lista
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.textName)
        val ultimaMsg: TextView = itemView.findViewById(R.id.textLastMessage)
        val foto: ImageView = itemView.findViewById(R.id.imageUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = lista[position]
        holder.nome.text = chat.nome
        holder.ultimaMsg.text = chat.ultimaMsg
        holder.foto.setImageResource(chat.foto)

        holder.itemView.setOnClickListener {
            onItemClick(chat)
        }
    }

    override fun getItemCount() = lista.size
}
