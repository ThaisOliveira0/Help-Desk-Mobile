package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.R
import com.example.projeto_suporte.databinding.ItemChamadoBinding
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

            binding.root.setOnClickListener { onClick(chamado) }

            val context = binding.root.context
            val cor = when (chamado.status) {
                "Aberto" -> ContextCompat.getColor(context, R.color.status_aberto)
                "Em Análise" -> ContextCompat.getColor(context, R.color.status_em_analise)
                "Fechado" -> ContextCompat.getColor(context, R.color.status_fechado)
                else -> ContextCompat.getColor(context, R.color.status_default) // Cor padrão
            }
            // Aplica a cor ao container principal do item
            binding.containerItemChamado.setBackgroundColor(cor)
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

    override fun getItemCount() = listaChamados.size

    fun updateData(novaLista: List<Chamado>) {
        this.listaChamados = novaLista
        notifyDataSetChanged()
    }
}
