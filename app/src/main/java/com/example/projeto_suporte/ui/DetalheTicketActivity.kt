package com.example.projeto_suporte.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityDetalheTicketBinding
import com.example.projeto_suporte.model.Chamado
import com.google.firebase.firestore.FirebaseFirestore

class DetalheTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalheTicketBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalheTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        val ticketId = intent.getStringExtra("ticket_id")

        if(ticketId == null) {
            Toast.makeText(this, "Erro: ID do chamado não encontrado.", Toast.LENGTH_LONG).show()
            finish() // Fecha a activity se não houver ID
            return
        }
        carregarDetalhesDoChamado(ticketId)

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun carregarDetalhesDoChamado(id: String) {
        db.collection("Chamados").document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Converte o documento do Firestore para o seu objeto Chamado
                    val chamado = document.toObject(Chamado::class.java)
                    if (chamado != null) {
                        // 3. Preenche a tela com os dados do chamado
                        preencherDados(chamado)
                    }
                } else {
                    Toast.makeText(this, "Chamado não encontrado.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao carregar detalhes: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("SetTextI18n")
    private fun preencherDados(chamado: Chamado) {
        binding.tvIdTicket.text = "ID do Ticket: ${chamado.numeroChamado}"
        binding.tvCategoria.text = "Categoria: ${chamado.categoria}"
        binding.tvData.text = "Criado em: ${chamado.dataAbertura}"
        binding.tvDescricao.text = chamado.descricao
    }
}