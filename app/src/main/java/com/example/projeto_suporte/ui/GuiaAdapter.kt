package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.databinding.ItemGuiaBinding

data class GuiaItem(
    val titulo: String,
    val imagem: Int,
    val descricao: String
)

class GuiaAdapter(
    private val lista: List<GuiaItem>,
    private val onItemClick: (GuiaItem) -> Unit
) : RecyclerView.Adapter<GuiaAdapter.GuiaViewHolder>() {

    inner class GuiaViewHolder(val binding: ItemGuiaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuiaViewHolder {
        val binding = ItemGuiaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuiaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuiaViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.apply {
            txtTitulo.text = item.titulo
            imgIcone.setImageResource(item.imagem)
            root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun getItemCount(): Int = lista.size
}
