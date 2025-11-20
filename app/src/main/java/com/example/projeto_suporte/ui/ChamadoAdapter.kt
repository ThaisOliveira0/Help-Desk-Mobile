package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.databinding.ItemChamadoBinding
import com.example.projeto_suporte.model.Chamado

class ChamadoAdapter(
    // 1. Mude de 'val' para 'var' para que a lista possa ser alterada
    private var listaChamados: List<Chamado>,
    private val onItemClick: (Chamado) -> Unit
) : RecyclerView.Adapter<ChamadoAdapter.ChamadoViewHolder>() {

    inner class ChamadoViewHolder(val binding: ItemChamadoBinding)
        : RecyclerView.ViewHolder(binding.root)

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
        val b = holder.binding

        b.txtIdChamado.text = "Chamado: ${chamado.numeroChamado}"
        b.txtAssunto.text = chamado.categoria
        b.txtData.text = chamado.dataAbertura
        b.imgIcone.setImageResource(android.R.drawable.ic_menu_manage)

        b.root.setOnClickListener {
            onItemClick(chamado)
        }
    }

    override fun getItemCount(): Int = listaChamados.size

    fun updateData(novaLista: List<Chamado>) {
        this.listaChamados = novaLista // Substitui a lista antiga pela nova
        notifyDataSetChanged()      // Notifica o adapter que os dados mudaram
    }
}
