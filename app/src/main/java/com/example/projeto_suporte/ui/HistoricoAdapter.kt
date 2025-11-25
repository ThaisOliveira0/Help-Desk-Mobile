package com.example.projeto_suporte.ui

import android.content.Intent // IMPORTAÇÃO NECESSÁRIA
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.databinding.ItemHistoricoCategoriaBinding
import com.example.projeto_suporte.databinding.ItemHistoricoChamadoBinding
import java.lang.IllegalArgumentException

class HistoricoAdapter(private val itens: List<ItemHistorico>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Constantes para os tipos de view
    companion object {
        private const val TIPO_CATEGORIA = 0
        private const val TIPO_CHAMADO = 1
    }

    // View Holder para o cabeçalho da categoria
    inner class CategoriaViewHolder(val binding: ItemHistoricoCategoriaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: ItemHistorico.CategoriaHeader) {
            binding.tvCategoriaHeader.text = header.nome
        }
    }

    // View Holder para o item do chamado
    inner class ChamadoViewHolder(val binding: ItemHistoricoChamadoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemHistorico.ChamadoItem) {
            binding.tvNumeroChamado.text = "Nº: ${item.numeroChamado}"
            binding.tvDataChamado.text = item.data

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetalheTicketActivity::class.java)

                // --- CORREÇÃO APLICADA AQUI ---
                // Altera a chave para "ticket_id", que é a que DetalheTicketActivity espera.
                intent.putExtra("ticket_id", item.id)

                context.startActivity(intent)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (itens[position]) {
            is ItemHistorico.CategoriaHeader -> TIPO_CATEGORIA
            is ItemHistorico.ChamadoItem -> TIPO_CHAMADO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TIPO_CATEGORIA -> {
                val binding = ItemHistoricoCategoriaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CategoriaViewHolder(binding)
            }
            TIPO_CHAMADO -> {
                val binding = ItemHistoricoChamadoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ChamadoViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Tipo de view inválido")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itens[position]) {
            is ItemHistorico.CategoriaHeader -> (holder as CategoriaViewHolder).bind(item)
            is ItemHistorico.ChamadoItem -> (holder as ChamadoViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = itens.size
}
