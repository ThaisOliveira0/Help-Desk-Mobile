package com.example.projeto_suporte.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.databinding.ItemFuncionarioRemocaoBinding
import com.example.projeto_suporte.models.Funcionario

class RemoverFuncionarioAdapter(
    private val funcionarios: MutableList<Funcionario>,
    private val onRemoveClick: (Funcionario) -> Unit
) : RecyclerView.Adapter<RemoverFuncionarioAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFuncionarioRemocaoBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFuncionarioRemocaoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val funcionario = funcionarios[position]

        holder.binding.tvNomeFuncionario.text = funcionario.nome

        holder.binding.btnRemoverFuncionario.setOnClickListener {
            onRemoveClick(funcionario)

            val index = holder.adapterPosition
            if (index != RecyclerView.NO_POSITION) {
                funcionarios.removeAt(index)
                notifyItemRemoved(index)
            }
        }
    }

    override fun getItemCount(): Int = funcionarios.size
}
