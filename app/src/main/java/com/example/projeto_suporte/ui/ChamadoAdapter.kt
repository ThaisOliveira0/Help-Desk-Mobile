package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.databinding.ItemChamadoBinding
import com.example.projeto_suporte.model.Chamado

class ChamadoAdapter(
    private val listaChamados: List<Chamado>,
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
}
