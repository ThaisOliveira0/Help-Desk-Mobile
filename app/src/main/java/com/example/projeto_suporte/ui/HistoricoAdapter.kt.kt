package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.R

class HistoricoAdapter(private val itens: List<ItemHistorico>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (itens[position]) {
            is ItemHistorico.CategoriaHeader -> TYPE_HEADER
            is ItemHistorico.ChamadoItem -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_categoria, parent, false)
            CategoriaViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_historico, parent, false)
            ChamadoViewHolder(view)
        }
    }

    override fun getItemCount(): Int = itens.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itens[position]) {
            is ItemHistorico.CategoriaHeader -> (holder as CategoriaViewHolder).bind(item)
            is ItemHistorico.ChamadoItem -> (holder as ChamadoViewHolder).bind(item)
        }
    }

    class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtCategoria: TextView = itemView.findViewById(R.id.txtCategoriaHeader)
        fun bind(item: ItemHistorico.CategoriaHeader) {
            txtCategoria.text = item.categoria
        }
    }

    class ChamadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtId: TextView = itemView.findViewById(R.id.textIdChamado)
        private val txtData: TextView = itemView.findViewById(R.id.textDataChamado)
        fun bind(item: ItemHistorico.ChamadoItem) {
            txtId.text = "Chamado #${item.id}"
            txtData.text = item.data
        }
    }
}
