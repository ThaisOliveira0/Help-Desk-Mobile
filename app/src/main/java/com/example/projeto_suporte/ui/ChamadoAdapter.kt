package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.R
import com.example.projeto_suporte.databinding.ItemChamadoBinding
import com.example.projeto_suporte.model.Chamado

class ChamadoAdapter(
    private var listaChamados: List<Chamado>,
    private val onItemClick: (Chamado) -> Unit
) : RecyclerView.Adapter<ChamadoAdapter.ChamadoViewHolder>() {

    inner class ChamadoViewHolder(val binding: ItemChamadoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // Movi a lógica de binding para dentro do ViewHolder
        fun bind(chamado: Chamado) {
            binding.txtIdChamado.text = "Chamado: ${chamado.numeroChamado}"
            binding.txtAssunto.text = chamado.categoria
            binding.txtData.text = chamado.dataAbertura
            binding.imgIcone.setImageResource(android.R.drawable.ic_menu_manage)

            binding.root.setOnClickListener {
                onItemClick(chamado)
            }

            // --- LÓGICA DE MUDANÇA DE COR ---
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChamadoViewHolder {
        val binding = ItemChamadoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChamadoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChamadoViewHolder, position: Int) {
        val chamado = listaChamados[position]
        // Chama a função bind para configurar o item
        holder.bind(chamado)
    }

    override fun getItemCount(): Int = listaChamados.size

    fun updateData(novaLista: List<Chamado>) {
        this.listaChamados = novaLista
        notifyDataSetChanged()
    }
}
