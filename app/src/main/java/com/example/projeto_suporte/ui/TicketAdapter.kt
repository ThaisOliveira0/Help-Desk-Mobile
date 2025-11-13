package com.example.projeto_suporte.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_suporte.R
import com.example.projeto_suporte.ui.models.Ticket

class TicketAdapter(
    private val lista: List<Ticket>,
    private val onClick: (Ticket) -> Unit
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIcone: ImageView = itemView.findViewById(R.id.imgIcone)
        val txtId: TextView = itemView.findViewById(R.id.txtIdChamado)
        val txtAssunto: TextView = itemView.findViewById(R.id.txtAssunto)
        val txtData: TextView = itemView.findViewById(R.id.txtData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chamado, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = lista[position]

        holder.txtId.text = "ID: ${ticket.id}"
        holder.txtAssunto.text = "Assunto: ${ticket.assunto}"
        holder.txtData.text = "Data: ${ticket.data}"

        holder.itemView.setOnClickListener { onClick(ticket) }
    }

    override fun getItemCount() = lista.size
}