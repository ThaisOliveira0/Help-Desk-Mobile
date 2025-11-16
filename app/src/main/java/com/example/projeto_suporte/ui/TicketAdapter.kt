package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.databinding.ItemChamadoBinding // <-- 1. Importe a classe de Binding do seu item
import com.example.projeto_suporte.model.Chamado

class TicketAdapter(
    private var listaChamados: List<Chamado>,
    private val onClick: (Chamado) -> Unit
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(private val binding: ItemChamadoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chamado: Chamado) {
            binding.txtIdChamado.text = "Chamado #${chamado.numeroChamado}"
            binding.txtAssunto.text = chamado.categoria
            binding.txtData.text = chamado.dataAbertura

            // Configura o clique no item
            binding.root.setOnClickListener { onClick(chamado) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemChamadoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val chamado = listaChamados[position]
        holder.bind(chamado)
    }

    fun updateData(novaLista: List<Chamado>) {
        this.listaChamados = novaLista
        notifyDataSetChanged()
    }

    override fun getItemCount() = listaChamados.size
}