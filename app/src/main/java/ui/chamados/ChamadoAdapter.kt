package com.example.projeto_suporte.ui.chamados

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.R
import com.example.projeto_suporte.model.Chamado

class ChamadoAdapter(private val listaChamados: List<Chamado>) :
    RecyclerView.Adapter<ChamadoAdapter.ChamadoViewHolder>() {

    inner class ChamadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtId: TextView = itemView.findViewById(R.id.txtIdChamado)
        val txtAssunto: TextView = itemView.findViewById(R.id.txtAssunto)
        val txtData: TextView = itemView.findViewById(R.id.txtData)
        val imgIcone: ImageView = itemView.findViewById(R.id.imgIcone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChamadoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chamado, parent, false)
        return ChamadoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChamadoViewHolder, position: Int) {
        val chamado = listaChamados[position]
        holder.txtId.text = "ID: ${chamado.id}"
        holder.txtAssunto.text = "Assunto: ${chamado.assunto}"
        holder.txtData.text = "Data: ${chamado.data}"
        holder.imgIcone.setImageResource(android.R.drawable.ic_menu_manage)
    }

    override fun getItemCount(): Int = listaChamados.size
}
