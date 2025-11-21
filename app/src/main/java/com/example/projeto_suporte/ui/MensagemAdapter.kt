package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.databinding.ItemMensagemEnviadaBinding
import com.example.projeto_suporte.databinding.ItemMensagemRecebidaBinding
import com.example.projeto_suporte.model.Mensagem // CRIE ESTE MODELO (passo seguinte)
import com.google.firebase.auth.FirebaseAuth

class MensagemAdapter(
    private var mensagens: List<Mensagem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    private val idUsuarioLogado: String = auth.currentUser?.uid ?: ""

    // Constantes para os tipos de view
    companion object {
        private const val TIPO_ENVIADA = 1
        private const val TIPO_RECEBIDA = 2
    }

    // View Holder para mensagens enviadas
    inner class EnviadaViewHolder(val binding: ItemMensagemEnviadaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mensagem: Mensagem) {
            binding.txtMensagemCorpo.text = mensagem.texto
        }
    }

    // View Holder para mensagens recebidas
    inner class RecebidaViewHolder(val binding: ItemMensagemRecebidaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mensagem: Mensagem) {
            binding.txtMensagemCorpo.text = mensagem.texto
        }
    }

    // A mágica acontece aqui! Este método decide qual layout usar.
    override fun getItemViewType(position: Int): Int {
        val mensagem = mensagens[position]
        return if (mensagem.remetenteId == idUsuarioLogado) {
            TIPO_ENVIADA
        } else {
            TIPO_RECEBIDA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TIPO_ENVIADA) {
            val binding = ItemMensagemEnviadaBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            EnviadaViewHolder(binding)
        } else { // TIPO_RECEBIDA
            val binding = ItemMensagemRecebidaBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            RecebidaViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mensagem = mensagens[position]
        if (holder.itemViewType == TIPO_ENVIADA) {
            (holder as EnviadaViewHolder).bind(mensagem)
        } else {
            (holder as RecebidaViewHolder).bind(mensagem)
        }
    }

    override fun getItemCount(): Int = mensagens.size

    fun updateData(novaLista: List<Mensagem>) {
        this.mensagens = novaLista
        notifyDataSetChanged()
    }
}
