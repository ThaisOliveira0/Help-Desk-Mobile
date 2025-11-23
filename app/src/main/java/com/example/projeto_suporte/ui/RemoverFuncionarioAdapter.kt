package com.example.projeto_suporte.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.databinding.ItemFuncionarioRemocaoBinding
import com.example.projeto_suporte.model.Usuario // Importe sua data class Usuario

class RemoverFuncionarioAdapter(
    private val funcionarios: MutableList<Usuario>,
    private val onExcluirClick: (Usuario) -> Unit // Função que será chamada ao clicar em excluir
) : RecyclerView.Adapter<RemoverFuncionarioAdapter.FuncionarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuncionarioViewHolder {
        val binding = ItemFuncionarioRemocaoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FuncionarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FuncionarioViewHolder, position: Int) {
        val funcionario = funcionarios[position]
        holder.bind(funcionario)
    }

    override fun getItemCount(): Int = funcionarios.size

    fun removerItem(position: Int) {
        funcionarios.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, funcionarios.size)
    }

    inner class FuncionarioViewHolder(private val binding: ItemFuncionarioRemocaoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(usuario: Usuario) {
            binding.tvNomeFuncionario.text = "${usuario.nome} ${usuario.sobrenome}"
            binding.tvEmailFuncionario.text = usuario.email

            binding.btnExcluir.setOnClickListener {
                // Pega a posição atual do item no adapter
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onExcluirClick(funcionarios[position])
                }
            }
        }
    }
}
